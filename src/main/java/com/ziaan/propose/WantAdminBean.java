//**********************************************************
//  1. 제      목: SUBJECT INFORMATION USER BEAN
//  2. 프로그램명: ProposeCourseBean.java
//  3. 개      요: 과정안내 사용자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 2007.12.14
//  7. 수      정:
//**********************************************************
package com.ziaan.propose;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.ziaan.propose.*;
import com.ziaan.course.*;
import com.ziaan.library.*;
import com.ziaan.common.*;
import com.ziaan.system.*;

public class WantAdminBean {
    public final static String GUBUN_CODE = "0049";
    public final static String WORK_CODE  = "W";
    public final static String LANG_CODE  = "L";

    public WantAdminBean() {}

    /** 관리자 - 수요조사관리 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectSubjWantDateList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1             = null;
        DataBox dbox 			= null;
        ArrayList list1         = null;
        String sql1             = "";
        String ss_edustart    	= StringManager.replace(box.getStringDefault("s_edustart", ""), "-", ""); // 학습시작
	    String ss_eduend 	    = StringManager.replace(box.getStringDefault("s_eduend", ""), "-", "");   // 학습종료
	    String ss_gyear         = box.getString("s_gyear");
	    String ss_title         = box.getString("s_title");
        String v_orderColumn    = box.getString       ("p_orderColumn"         );   // 정렬할 컬럼명
        String v_orderType      = box.getString       ("p_orderType"           );   // 정렬할 순서

        try {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1 = "select year, seq, startdate, enddate, title from tz_subjwant_master where gubun ='Y' \n";
						// 년도
	        if (!ss_gyear.equals("ALL")) {
	        	sql1 += "   and year  ='"+ss_gyear+"' \n";

		        // 시작일
		        if (!ss_edustart.equals("")) {
		        	sql1 += "   and startdate  >="+ss_edustart+" \n";

			      	// 종료일
			        if (!ss_eduend.equals("")) {
			        	sql1 += "   and enddate  <='"+ss_eduend+"' \n";
			        }
		        }
	        }

	      	// 조사명
	        if (!ss_title.equals("")) {
	        	sql1 += "   and title like '%" + ss_title + "%' \n";
	        }
	        
	        if( v_orderColumn.equals("") ){
	        	sql1 += " order   by seq desc \n";
	        } else { 
	        	sql1 += " order   by " + v_orderColumn + v_orderType + " \n";
            }

            ls1 = connMgr.executeQuery(sql1);
          //  Log.info.println("sql1_______________________________>"+sql1);
            while(ls1.next()) {
            	dbox = ls1.getDataBox();

            	list1.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list1;
    }

	/**
	* 새로운 자료실 내용 등록
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/
	 public int insertWant(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		PreparedStatement pstmt1 = null;
		StringBuffer sql1 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		int	isOk1 =	0;
		int idx = 1;

		String v_luserid 	= box.getSession("userid");

    	String v_edustart 	= StringManager.replace(box.getStringDefault("p_edustart", ""), "-", ""); // 학습시작
    	String v_eduend 	= StringManager.replace(box.getStringDefault("p_eduend", ""), "-", ""); // 학습종료

    	String v_gyear      = box.getString("s_gyear");
		String v_title    	= box.getString("p_title");
		int v_maxseq = 1;

		try	{
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			sql1.append("select decode(max(seq),'',1,max(seq)+1) maxseq from tz_subjwant_master where year = '"+v_gyear+"'");
            ls = connMgr.executeQuery(sql1.toString());

            while (ls.next()) {
            	v_maxseq = ls.getInt("maxseq");
            }
			if ( ls != null ) { try { ls.close(); } catch ( Exception e1 ) { } }

			sql2.append("insert into tz_subjwant_master ");
			sql2.append("	(year, seq, startdate, enddate, gubun, title, ");
			sql2.append("	 indate, inuserid, ldate, luserid) ");
			sql2.append(" values (?, ?, ?, ?,'Y', ?, ");
			sql2.append("	 to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?) ");

			pstmt1 = connMgr.prepareStatement(sql2.toString());

			pstmt1.setString(idx++, v_gyear);
			pstmt1.setInt	(idx++, v_maxseq);
			pstmt1.setString(idx++, v_edustart);
			pstmt1.setString(idx++, v_eduend);
			pstmt1.setString(idx++, v_title);
			pstmt1.setString(idx++, v_luserid);
			pstmt1.setString(idx++, v_luserid);

			isOk1 =	pstmt1.executeUpdate();

			isOk1 = isOk1 * insertWantSubj(connMgr, box, v_maxseq);

			if ( isOk1 > 0) connMgr.commit();
			else 		    connMgr.rollback();
		}
		catch ( Exception ex ) {
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex,	box, sql1.toString());
			throw new Exception("sql = " + sql1.toString()	 + "\r\n" + ex.getMessage() );
		}
		finally	{
			if ( ls != null ) { try { ls.close(); } catch ( Exception e1 ) { } }
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return isOk1;
	}


    /**
    강사에 해당하는 강의과정등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertWantSubj(DBConnectionManager connMgr, RequestBox box, int v_seq) throws Exception {
		PreparedStatement pstmt1 = null;
        String sql1                  = "";
        String sql2                  = "";
        String v_user_id            = box.getSession("userid");

        //p_subj로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_subj               = new Vector();
        v_subj                      = box.getVector("p_subj");
        Enumeration em              = v_subj.elements();
        String v_eachSubj           = "";   //실제 넘어온 각각의 과정코드

    	String v_gyear      = box.getString("s_gyear");

        int isOk                    = 0;
        int idx = 1;

        try{

            //delete TZ_SUBJMAN table
            sql1 = "delete from TZ_SUBJWANT_SUBJ where year='" + v_gyear + "' and seq=" + v_seq;
            isOk = connMgr.executeUpdate(sql1);

            sql2 =  "insert into TZ_SUBJWANT_SUBJ(year, seq, subj, inuserid, indate) ";
            sql2+=  "values(?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

			pstmt1 = connMgr.prepareStatement(sql2);

            while(em.hasMoreElements()){
                v_eachSubj   = (String)em.nextElement();

                idx = 1;

				pstmt1.setString(idx++, v_gyear);
				pstmt1.setInt	(idx++, v_seq);
				pstmt1.setString(idx++, v_eachSubj);
				pstmt1.setString(idx++, v_user_id);

				isOk =	pstmt1.executeUpdate();
            }
        }
        catch(Exception ex) {
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
		finally	{
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
		}
        return isOk;
    }

	/**
	*  내용 삭제 업데이트
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/
	 public int delWant(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		PreparedStatement pstmt1 	= null;
		String            sql1		= "";
		int	isOk1 =	1;

		String v_content1 	= "";
		String v_gubun 		= "02";//box.getString("p_gubun");
		String v_enddate	= box.getString("p_enddate");
		String v_subj		= box.getString("p_wantsubj");

    	String ss_edustart 	= StringManager.replace(box.getStringDefault("s_edustart", ""), "-", ""); // 학습시작
    	String ss_eduend 	= StringManager.replace(box.getStringDefault("s_eduend", ""), "-", ""); // 학습종료

    	String  ss_gyear    = box.getString("s_year");
    	String  ss_seq    	= box.getString("s_seq");

		try	{
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			//// //// //// //// //// //// //// //// // 	table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql1 ="update tz_subjwant_master set gubun = ?";
			sql1 +=	" where year = "+ss_gyear;
			sql1 +=	" and seq = "+ss_seq;

			pstmt1 = connMgr.prepareStatement(sql1);

			pstmt1.setString(1,"N");

			isOk1 =	pstmt1.executeUpdate();

			if ( isOk1 > 0) connMgr.commit();
			else 		               connMgr.rollback();
		}
		catch ( Exception ex ) {
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex,	box, sql1);
			throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
		}
		finally	{
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return isOk1;
	}


/**
	*  내용 삭제 업데이트
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/
	 public int updateWant(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		PreparedStatement pstmt1 = null;
		String            sql1   = "";
		int	isOk1 =	1;

		String v_content1 	= "";
		String v_gubun 		= "02";//box.getString("p_gubun");
		String v_enddate	= box.getString("p_enddate");
		String v_subj		= box.getString("p_wantsubj");

	    String ss_edustart = StringManager.replace(box.getStringDefault("p_edustart", ""), "-", ""); // 학습시작
	    String ss_eduend   = StringManager.replace(box.getStringDefault("p_eduend", ""), "-", ""); // 학습종료

	    int     ss_seq    	= box.getInt("s_seq");
	    String  ss_gyear    = box.getString("s_gyear");
	    String  v_title    	= box.getString("p_title");
	    String  v_luserid	= box.getSession("userid");

	    int idx = 1;

		try	{
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			//// //// //// //// //// //// //// //// // 	table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql1 ="update tz_subjwant_master set startdate = ? , enddate = ?, ";
			sql1 +=	" 	title = ? ,";
			sql1 +=	" 	ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ,";
			sql1 +=	" 	luserid = ? ";
			sql1 +=	" where year = ?";
			sql1 +=	" and seq = ? ";

			pstmt1 = connMgr.prepareStatement(sql1);
			idx = 1;
			pstmt1.setString(idx++, ss_edustart);
			pstmt1.setString(idx++, ss_eduend);
			pstmt1.setString(idx++, v_title);
			pstmt1.setString(idx++, v_luserid);
			pstmt1.setString(idx++, ss_gyear);
			pstmt1.setInt	(idx++, ss_seq);

			isOk1 =	pstmt1.executeUpdate();

			isOk1 = isOk1 * insertWantSubj(connMgr, box, ss_seq);

			if ( isOk1 > 0) connMgr.commit();
			else 		               connMgr.rollback();
		}
		catch ( Exception ex ) {
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex,	box, sql1);
			throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
		}
		finally	{
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return isOk1;
	}


   /**
   * 연간수요조사 과정리스트
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
   * @throws Exception
   */
   public ArrayList WantSubjList(RequestBox box)	throws Exception {
		DBConnectionManager	connMgr	= null;
        ArrayList list1 = null;
		ListSet	ls 		= null;
        DataBox dbox 	= null;
		String  sql     = "";

	  	String  ss_gyear 	= box.getString("s_year");
    	String  ss_seq    	= box.getString("s_seq");

		try	{
			connMgr	= new DBConnectionManager();
            list1 = new ArrayList();

			sql =  " select a.subj, b.subjnm from tz_subjwant_SUBJ a, tz_subj b ";
			sql += " where a.subj = b.subj and year = "+ss_gyear;
			sql += " 	and seq ="+ ss_seq;
			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) {
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
		}
		catch ( Exception ex ) {
			ErrorManager.getErrorStackTrace(ex,	box, sql);
			throw new Exception("sql = " + sql + "\r\n"	 + ex.getMessage() );
		}
		finally	{
			if ( ls != null ) { try	{ ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e95 ) { }	}
		}
		return list1;
	}


   /**
   * 선택된	자료실 게시물 상세내용 select
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
   * @throws Exception
   */
   public DataBox selectwant(RequestBox box)	throws Exception {
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		String  sql     = "";
		DataBox dbox    = null;

	  	String  ss_gyear 	= box.getString("s_year");
    	String  ss_seq    	= box.getString("s_seq");

		try	{
			connMgr	= new DBConnectionManager();

			sql =	 " select year , seq , startdate , enddate, title from tz_subjwant_master where gubun ='Y' ";
			sql += " and year	="+ss_gyear;
			sql += " and	seq 	="+ ss_seq;
			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) {
                dbox = ls.getDataBox();
            }
		}
		catch ( Exception ex ) {
			ErrorManager.getErrorStackTrace(ex,	box, sql);
			throw new Exception("sql = " + sql + "\r\n"	 + ex.getMessage() );
		}
		finally	{
			if ( ls != null ) { try	{ ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e95 ) { }	}
		}
		return dbox;
	}

    /** 관리자 - 과정안내
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectSubjWantList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1             = null;
        DataBox dbox 			= null;
        ArrayList list1         = null;
        //String sql1             = "";
        StringBuffer strSQL = null;
        String  v_user_id       = box.getSession("userid");

        // 개인의 회사 기준으로 과정 리스트
        String  v_comp      = box.getSession("comp");

        // 사이트의 GRCODE 로 과정 리스트
        String v_grcode = box.getSession("tem_grcode");
        String ss_gyear = box.getString("s_gyear");
        String ss_subj = box.getString("s_subjcourse");

        try {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            /*
			sql1 = "	select b.subj,b.subjnm,a.comp,a.compnm,decode(b.cnt,'',0,b.cnt) cnt \n";
			sql1 += "	from ( \n";
			sql1 += "			select comp,compnm,seq \n";
			sql1 += "			from tz_comp a \n";
			//sql1 += "			where comptype = '2' \n";
			sql1 += "		 ) A, \n";
			sql1 += "		( \n";
			sql1 += "			select a.subj,d.subjnm,c.comp ,count(a.userid) cnt 	\n";
			sql1 += "			from tz_subjwant_user a, tz_member b , tz_comp c , tz_subj d \n";
			sql1 += "			where a.userid = b.userid and b.comp = c.comp and a.subj = d.subj \n";
			// 년도
	        if (!ss_gyear.equals("ALL")) {
	        	sql1 += "   and a.appdate like '" + ss_gyear + "%' \n";
	        }
	        // 과정
	        if (!ss_subj.equals("ALL")) {
	        	sql1 += "   and a.subj = " + SQLString.Format(ss_subj);
	        }
			sql1 += "			group by a.subj, c.comp, d.subjnm \n";
			sql1 += "		)b \n";
			sql1 += "	where a.comp = b.comp(+) \n";
			sql1 += "	order by b.subj,seq,a.compnm  \n";

            ls1 = connMgr.executeQuery(sql1);
			*/
            
            strSQL = new StringBuffer();
            strSQL.append("  select b.subjnm, a.comp, a.compnm, decode(a.cnt,'',0,a.cnt) cnt ") ;
            strSQL.append("    from ( ") ;
            strSQL.append("           select aa.subj, cc.comp, cc.compnm, count(aa.userid) cnt ") ;
            strSQL.append("             from TZ_SUBJWANT_USER aa, tz_member bb, tz_compclass cc ") ;
            strSQL.append("             where aa.userid = bb.userid ") ;
            strSQL.append("               and bb.comp = cc.comp ") ;
			// 년도
	        if (!ss_gyear.equals("ALL")) {
	        	strSQL.append("           and aa.appdate like '" + ss_gyear + "%' ") ;
	        }
            strSQL.append("             group by aa.subj, cc.comp, cc.compnm ") ;
            strSQL.append("         ) a, tz_subj b ") ;
            strSQL.append("   where a.subj = b.subj ") ;
            
	        // 과정
	        if (!ss_subj.equals("ALL")) {
	        	strSQL.append(" and a.subj = " + SQLString.Format(ss_subj)) ;
	        }
            
	        ls1 = connMgr.executeQuery(strSQL.toString());
	        
            while(ls1.next()) {
            	dbox = ls1.getDataBox();

            	list1.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("strSQL = " + strSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list1;
    }

        /**
    상세 조회
    @param box          receive from the form object and session
    @return DataBox
    */
    public DataBox subjselect(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet				ls			= null;
        String				sql			= "";

        String ss_subj = box.getString("s_subjcourse");

        try {
            connMgr     = new DBConnectionManager();

            sql  = "SELECT 	subj, subjnm ,indate ";
            sql += "FROM		TZ_SUBJ ";
            sql += "WHERE		subj = "+ SQLString.Format(ss_subj);
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                dbox = ls.getDataBox();
            }
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if ( connMgr != null ) {  try {  connMgr.freeConnection();  } catch (Exception e10 ) { } }
        }

        return dbox;
    }



    /** 관리자 - 과정안내
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectSubjWantList2(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1             = null;
        DataBox dbox 			= null;
        ArrayList list1         = null;
        String sql1             = "";
        String  v_user_id       = box.getSession("userid");

        // 개인의 회사 기준으로 과정 리스트
        String  v_comp      = box.getString("s_comp");

        // 사이트의 GRCODE 로 과정 리스트
        String v_grcode = box.getSession("tem_grcode");
        String ss_gyear = box.getString("s_gyear");
        String ss_subj  = box.getString("s_subjcourse");
        String ss_year  = box.getString("s_year");
        String ss_seq  = box.getString("s_seq");

        try {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

			sql1 = "select subj, subjnm, a.comp, pcodenm, a.compnm, decode(b.cnt,'',0,b.cnt) cnt, comptype, pcode, ppcode, cntpcode	\n";
			sql1 +=" from (select pcodenm,comp, compnm, seq, comptype, pcode, ppcode \n";
			sql1 +=" 	 		 , (select count(pcode) from tz_comp where pcode = a.pcode and ppcode = a.ppcode group by pcode) cntpcode \n";
			sql1 +=" 	  from tz_comp a	\n";
			sql1 +=" 	  where comp = '" + v_comp + "'	\n";
			sql1 +=" 	  		or pcode = '" + v_comp + "'\n";
			sql1 +=" 			or ppcode='" + v_comp + "') A,\n";
			sql1 +=" 	 (select a.subj,c.subjnm,b.comp ,count(a.userid) cnt \n";
			sql1 +=" 		from tz_subjwant_user a, tz_member b ,tz_subj c \n";
			sql1 +=" 		where a.userid = b.userid  \n";
			sql1 +=" 			and c.subj = a.subj(+) \n";
			sql1 +=" 			and a.year = '"+ss_year+"' \n";
			sql1 +=" 			and a.seq = '"+ss_seq+"' \n";
				/*	// 년도
	        if (!ss_gyear.equals("ALL")) {
	        	sql1 += "   and a.appdate like '" + ss_gyear + "%' \n";
	        }*/
	        // 과정
	        if (!ss_subj.equals("ALL")) {
	        	sql1 += "   and a.subj =" + SQLString.Format(ss_subj);
	        }

			sql1 += " group by a.subj, b.comp, c.subjnm	\n";
			sql1 += " )b \n";
			sql1 += " where a.comp=b.comp(+)	\n";
			sql1 += " order by a.pcode,seq	\n";

            ls1 = connMgr.executeQuery(sql1);

            while(ls1.next()) {
            	dbox = ls1.getDataBox();

            	list1.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list1;
    }

/** 관리자 - 수요조사결과 (과정명에 해당하는 신청인원에대한 목록)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectSubjWantList3(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1             = null;
        DataBox dbox 			= null;
        ArrayList list1         = null;
        StringBuffer strSQL     = null;
        //String sql1             = "";
        String  v_user_id       = box.getSession("userid");
		String	ss_action		= box.getString("p_action");
        String  v_comp          = box.getString("p_comp");
        
        /*
        if(ss_action.equals("select")){
        	v_comp = box.getString("s_comp");
		}
		else{
			v_comp = box.getString("p_comp");
		}
		*/

        // 사이트의 GRCODE 로 과정 리스트
        String v_grcode = box.getSession("tem_grcode");
        String ss_gyear = box.getString("s_gyear");
        String ss_subj  = box.getString("s_subjcourse");
        String ss_year  = box.getString("s_year");
        String ss_seq  = box.getString("s_seq");
        
        String v_orderColumn    = box.getString       ("p_orderColumn"         );   // 정렬할 컬럼명
        String v_orderType      = box.getString       ("p_orderType"           );   // 정렬할 순서

        try {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            
            /*
			sql1 = "		 select subj,subjnm,a.comp,a.compnm,userid,	appdate,jikwinm,name, comptype , b.jikryul , b.jikupnm , b.deptnam \n";
			sql1 += "		from \n";
			sql1 += "			 ( \n";
			sql1 += "				select comp,compnm,seq,comptype \n";
			sql1 += "				from tz_comp a \n";
			sql1 += " 	  			where comp = '" + v_comp + "') A, \n";
			sql1 += "			( \n";
			sql1 += "			 	select a.subj, c.subjnm, b.comp, a.userid, a.appdate,get_jikryulnm(b.jikryul) jikryul, get_jikupnm(b.jikup, b.comp) jikwinm, name , \n";
			sql1 += "			 	get_jikupnm(b.jikryul,b.jikup) jikupnm , deptnam  \n";
			sql1 += "				from tz_subjwant_user a, tz_member b, tz_subj c \n";
			sql1 += "				where a.userid = b.userid and c.subj = a.subj(+) \n";
			sql1 +=" 			and a.year = '"+ss_year+"' \n";
			sql1 +=" 			and a.seq = '"+ss_seq+"' \n";

	        // 과정
	        if (!ss_subj.equals("ALL")) {
	        	sql1 += "   and a.subj =" + SQLString.Format(ss_subj);
	        }
			sql1 += "			)b \n";
			sql1 += "	where a.comp = b.comp \n";
			sql1 += " order by seq  ,b.deptnam, b.jikryul , b.jikupnm \n";
			*/
            
            strSQL = new StringBuffer();

            strSQL.append("select subj, subjnm, compnm, position_nm, userid, name, appdate \n ") ;
            strSQL.append(" from ( \n ") ;
            strSQL.append("        select a.subj, a.subjnm, d.compnm, c.position_nm, b.userid, c.name, b.appdate \n ") ;
            strSQL.append("          from tz_subj a, tz_subjwant_user b, tz_member c, tz_compclass d \n ") ;
            strSQL.append("         where a.subj=b.subj \n ") ;
            strSQL.append("           and b.userid=c.userid \n ") ;
            strSQL.append("           and c.comp=d.comp \n ") ;
            strSQL.append("           and year='"+ss_year+"' \n ") ;
            strSQL.append("           and seq = '"+ss_seq+"' \n ") ;
            strSQL.append("           and a.subj = '"+ss_subj+"' \n ") ;
            strSQL.append("           and d.comp='"+v_comp+"' \n ") ;
            strSQL.append("      ) \n ") ;
            
	        if( v_orderColumn.equals("") ){
	        	strSQL.append(" order by subjnm, compnm, position_nm, userid, appdate ") ;
	        } else { 
	        	strSQL.append(" order   by " + v_orderColumn + v_orderType );
            }

            ls1 = connMgr.executeQuery(strSQL.toString());

            while(ls1.next()) {
            	dbox = ls1.getDataBox();

            	list1.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("sql1 = " + strSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list1;
    }

    /** 관리자 - 과정수요조사
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectWantSubjList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1             = null;
        DataBox dbox 			= null;
        ArrayList list1         = null;
        String sql1             = "";
        String ss_year          = box.getString("s_year");
        String ss_seq           = box.getString("s_seq");
        String v_orderColumn    = box.getString("p_orderColumn");   // 정렬할 컬럼명
        String v_orderType      = box.getString("p_orderType");   // 정렬할 순서

        String ss_gyear = box.getString("s_gyear");
        try {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

			sql1 =  "	select  a.subjnm , a.subj , count(b.userid) cnt 						\n";
			sql1 += "	 from tz_subj a , (select subj , userid , appdate from tz_subjwant_user where  year = '"+ss_year+"' \n";
			sql1 += " and seq = '"+ss_seq+"' ";

			/* 년도
	        if (!ss_gyear.equals("ALL")) {
	        	sql1 += "   and appdate like '" + ss_gyear + "%' \n";
	        }
	    */
			sql1 += " ) b , 						\n";
			sql1 += " TZ_SUBJWANT_SUBJ c 						\n";
			sql1 += "	where a.subj = b.subj(+)							\n";
			sql1 += "	and a.subj = c.subj							\n";
			sql1 += "	and c.seq = '"+ss_seq+"' ";
			sql1 += "	group by a.subjnm, a.subj 						\n";

	        if( v_orderColumn.equals("") ){
	        	sql1 += " order   by a.subjnm \n";
	        } else { 
	        	sql1 += " order   by " + v_orderColumn + v_orderType + " \n";
            }
			
			Log.info.println("수요 = "+sql1);
            ls1 = connMgr.executeQuery(sql1);

            while(ls1.next()) {
            	dbox = ls1.getDataBox();

            	list1.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list1;
    }

    /** 사용자 - 과정수요조사 체크
    @param box      receive from the form object and session
    @return ArrayList
    */
     public static int chkWantDate() throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1             = null;
        String sql1             = "";
        int v_rtnvalue = 0;

        try {
            connMgr = new DBConnectionManager();

			sql1 = " select count(seq) cnt from tz_subjwant_master \n";
			sql1 +=" where to_number(startdate) <= to_number(to_char(sysdate, 'yyyyMMdd')) \n";
			sql1 +=" 	and to_number(enddate) >= to_number(to_char(sysdate, 'yyyyMMdd')) \n";

            ls1 = connMgr.executeQuery(sql1);

            while(ls1.next()) {
            	v_rtnvalue = ls1.getInt("cnt");
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return v_rtnvalue;
    }

}