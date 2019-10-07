// **********************************************************
// 1. 제      목: 수강신청관련 테이블 Insert/delete/update
// 2. 프로그램명: ProposeBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: 이창훈 2004-11-5
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.propose;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.common.SubjComBean;
import com.ziaan.course.SubjGongData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.MSSQL_DBConnectionManager;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.scorm2004.ScormCourseBean;
import com.ziaan.system.CodeAdminBean;
import com.ziaan.system.SelectionUtil;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class ProposeBean { 
    public ArrayList SelectAcceptTargetMember(RequestBox box) throws Exception {   // 직접입과처리시 화면 List
        ArrayList list = new ArrayList();

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ResultSet rs = null;
        ListSet             ls      = null;
        String sql1  = "";
        String sql2  = "";
        DataBox             dbox    = null;

        String v_searchtext = box.getString("p_searchtext");
        String v_grcode   = box.getString("s_grcode");
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getString("s_grseq");
        String v_subj     = box.getString("s_subjcourse");
        String v_subjseq  = box.getString("s_subjseq");
        String v_year     = getSubjYear(v_grcode, v_gyear, v_grseq, v_subj, v_subjseq);
        String v_chkfirst = "N";
        String v_chkfinal = "N";
        String v_isdinsert = "";
        String v_isproposeapproval = "";
        String v_useproposeapproval = "";
        String v_cancelkind = "";

        v_useproposeapproval = getProposeApproval(v_subj,v_year,v_subjseq);

        if ( v_year.equals("")) v_year = v_gyear;

        try { 
            if ( box.getString("p_action").equals("go") ) { 
                sql1  = " select a.group_id as asgn 								\n";
                sql1 += "      , a.lvl_nm as jikwinm 								\n";
                sql1 += "      , get_compnm(a.group_id,2,2) asgnnm 					\n";
                sql1 += "      , a.post as jikup 									\n";
                sql1 += "      , a.post_nm as jikupnm 								\n";
                sql1 += "      , a.userid 											\n";
                sql1 += "      , a.name 											\n";
                sql1 += " from   tz_member  a  										\n";
                sql1 += " where  (a.userid like " + SQLString.Format("%" +v_searchtext + "%") + " or a.name like " + SQLString.Format("%" +v_searchtext + "%") + ")";
                sql1 += " order  by a.name ";

                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql1);

                sql2  = "select a.subj, a.year, a.subjseq, a.course, a.cyear, a.courseseq, b.chkfirst, b.chkfinal,b.cancelkind, b.isproposeapproval, b.isdinsert ";
                sql2 += "  from tz_subjseq  a,  ";
                sql2 += "       tz_propose  b ";
                sql2 += " where a.subj = b.subj ";
                sql2 += "   and a.year = b.year ";
                sql2 += "   and a.subjseq = b.subjseq ";
                sql2 += "   and b.userid =  ? ";

                pstmt = connMgr.prepareStatement(sql2);

                while ( ls.next() ) { 
                    v_chkfirst = "N";
                    v_chkfinal = "";
                    v_isproposeapproval = "";
                    v_isdinsert   = "";

                    pstmt.setString(1, ls.getString("userid") );
                    rs = pstmt.executeQuery();
                    while ( rs.next() ) { 
                        if ( (v_subj.equals(rs.getString("subj")) && v_year.equals(rs.getString("year")) && v_subjseq.equals(rs.getString("subjseq")) ) ||
                            (v_subj.equals(rs.getString("course")) && v_year.equals(rs.getString("cyear")) && v_subjseq.equals(rs.getString("courseseq")))) { 
                            v_chkfirst = rs.getString("chkfirst");
                            v_chkfinal = rs.getString("chkfinal");
                            v_cancelkind = StringManager.chkNull(rs.getString("cancelkind") );
                            v_isproposeapproval = rs.getString("isproposeapproval");
                            v_isdinsert   = rs.getString("isdinsert");
                            break;
                        }
                    }
                    dbox = ls.getDataBox();

                    dbox.put("d_chkfirst", v_chkfirst);
                    dbox.put("d_chkfinal", v_chkfinal);
                    dbox.put("d_cancelkind", v_cancelkind);
                    dbox.put("d_isdinsert", v_isdinsert);
                    dbox.put("d_isproposeapproval", v_isproposeapproval);
                    dbox.put("d_useproposeapproval", v_useproposeapproval);
                    list.add(dbox);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    새로운 과목코드 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int Accept(RequestBox box) throws Exception { 
        // 등록권한 체크
        DBConnectionManager	connMgr	= null;

        int isOk = 0;
        String v_grcode   = box.getString("s_grcode");
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getString("s_grseq");
        String v_subj     = box.getString("s_subjcourse");
        String v_subjseq  = box.getString("s_subjseq");

        String v_isdinsert  = "Y";                     // 강제입력여부

        // 등록권한별 1차승인, 2차승인 결정
        String v_chkfirst   = "Y";
        String v_chkfinal   = "Y";
        // String v_useproposeapproval = "N";
        // String v_isproposeapproval = "L";

        String  v_year = getSubjYear(v_grcode, v_gyear, v_grseq, v_subj, v_subjseq);
        if ( v_year.equals("")) v_year = v_gyear;

        String v_userid  = "";

        boolean isedutarget = false;
        boolean ismastersubj = false;


        Vector v_checks  = box.getVector("p_checks");
        Enumeration em  = v_checks.elements();
        Hashtable insertData = new Hashtable();
        SubjComBean csbean = new SubjComBean();
        ProposeCourseBean pcbean  = new ProposeCourseBean();


        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_userid = (String)em.nextElement();

                insertData.clear();
                insertData.put("connMgr",  connMgr);
                insertData.put("subj",     v_subj);
                insertData.put("year",     v_year);
                insertData.put("subjseq",  v_subjseq);
                insertData.put("userid",   v_userid);
                insertData.put("isdinsert",v_isdinsert);
                insertData.put("isdinsert",v_isdinsert);
                insertData.put("chkfirst", v_chkfirst);
                insertData.put("chkfinal", v_chkfinal);
                insertData.put("luserid",  box.getSession("userid") );
                insertData.put("box",      box);

                isOk = insertPropose(insertData);
                isOk = insertStudent(insertData);

                isedutarget  = csbean.IsEduTarget(v_subj, v_year, v_subjseq, v_userid);
                ismastersubj = csbean.IsMasterSubj(v_subj, v_year, v_subjseq);


                if ( ismastersubj&&!isedutarget) {    // 대상자매핑과목이나 대상자가 선정이 되지 않았을때
                    insertData.put("mastercd", csbean.getMasterCode(v_subj, v_year, v_subjseq));
                    isOk = insertEduTarget(insertData);
                }

            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, "Propose.Cancel");
            throw new Exception("ProposeBean.Accept" + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

/*---- 추가 블랙리스트--??? 본서버에서 가져옴--*/
    /**
    새로운 과목코드 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int insertBlackCondition(RequestBox box) throws Exception { 
        // 등록권한 체크
        int isOk = 0;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet ls         = null;
        String v_seq        = "";
        String v_company    = box.getString("p_company");
        String v_upperclass = box.getString("p_upperclass");
        String v_propstart  = box.getString("s_propstart");
        String v_propend    = box.getString("s_propend");
        String v_isuse      = box.getString("p_isuse");
        String              sql     = "";
        String sql1 = "";


        // insert TZ_EDUTARGET table
        sql =  "insert into tz_blackcondition(seq,company,upperclass,propstart,propend,isuse ) values(?, ?, ?, ?, ?, ?)" ;
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  =" select nvl(ltrim(rtrim(to_char(to_number(max(SEQ)) +1,'0000000000'))),'0000000001') MSTCD " ;
            sql1 += "From TZ_BLACKCONDITION ";

            ls = connMgr.executeQuery(sql1);
            if ( ls.next() ) { 
                v_seq = ls.getString("MSTCD");
            }
            else{ 
                v_seq = "0000000001";
            }


            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_seq);
            pstmt.setString(2, v_company);
            pstmt.setString(3, v_upperclass);
            pstmt.setString(4, v_propstart);
            pstmt.setString(5, v_propend); // ldate
            pstmt.setString(6, v_isuse); // ldate

            isOk = pstmt.executeUpdate();
            
            if ( isOk > 0) { 
              connMgr.commit();
            } else { 
              connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, "Propose.Cancel");
            throw new Exception("ProposeBean.Accept" + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
        }

        return isOk;
    }

    public int insertEduTarget(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String sql1 = "";

        String v_grcode            = (String)data.get("grcode");           // 교육그룹
        String v_gyear             = (String)data.get("gyear");            // 년도
        String v_grseq             = (String)data.get("grseq");            // 교육기수
        String v_subj              = (String)data.get("subjcourse");
        String v_mastercd          = (String)data.get("mastercd");
        String v_subjseq           = (String)data.get("subjseq");
        String v_subjnm            = (String)data.get("subjnm");
        String v_isproposeapproval = (String)data.get("isproposeapproval");
        String v_userid            = (String)data.get("userid");
        String v_luserid           = (String)data.get("luserid");

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        // insert TZ_EDUTARGET table
        sql =  "insert into TZ_EDUTARGET ( " ;
        sql +=  " mastercd,  userid,  isproposeapproval, ";
        sql +=  " luserid, ldate ) " ;
        sql +=  " values ( ";
        sql +=  " ?, ?, ?, ";
        sql +=  " ?, ? ) ";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            pstmt = (PreparedStatement)data.get("edutarget_pstmt");
            if ( pstmt == null ) { 
                pstmt = connMgr.prepareStatement(sql);
                v_CreatePreparedStatement = true;
            }

                pstmt.setString( 1, v_mastercd);
                pstmt.setString( 2, v_userid);
                pstmt.setString( 3, "M");
                pstmt.setString( 4, v_luserid);
                pstmt.setString( 5, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate

                isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( v_CreatePreparedStatement) { 
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            }
        }

        return isOk;
    }

    public int insertAutoAssignTemp(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";

        ListSet ls1         = null;

        String v_mastercd          = (String)data.get("mastercd");
        String v_subj              = (String)data.get("subjcourse");
        String v_subjseq           = (String)data.get("subjseq");
        String v_year              = (String)data.get("year");
        String v_userid            = (String)data.get("userid");
        String v_luserid           = (String)data.get("luserid");

        int cnt = 0;

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            sql1 = "select count(subj) cnt from TZ_AutoAssignTemp where mastercd=" +SQLString.Format(v_mastercd) + " and userid=" +SQLString.Format(v_userid);
            ls1 = connMgr.executeQuery(sql1);

            if ( ls1.next() ) { cnt = ls1.getInt("cnt"); }   // 취소한 횟수

            if ( cnt == 0 ) { 
                // insert TZ_EDUTARGET table
                sql =  "insert into TZ_EDUTARGET ( " ;
                sql +=  " mastercd,  userid,  subj, subjseq, year, ";
                sql +=  " luserid, ldate ) " ;
                sql +=  " values ( ";
                sql +=  " ?, ?, ?, ?, ?";
                sql +=  " ?, ? ) ";

                pstmt = connMgr.prepareStatement(sql);

                pstmt.setString( 1, v_mastercd);
                pstmt.setString( 2, v_userid);
                pstmt.setString( 3, v_subj);
                pstmt.setString( 4, v_subjseq);
                pstmt.setString( 5, v_year);
                pstmt.setString( 6, v_luserid);
                pstmt.setString( 7, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate

                isOk = pstmt.executeUpdate();
             }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }



    /**
    수강 신청
    @param Hashtable   수강신청 정보
    @return int        1:insert success,0:insert fail
    */
    public int insertPropose(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String 				sql1 	= "";
        String 				sql2 	= "";
        ListSet             ls1     = null;
        
        String             order_id = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");

        String v_userid = (String)data.get("userid");
        String v_isdinsert = (String)data.get("isdinsert");
        // String v_isproposeapproval = (String)data.get("isproposeapproval");
        String v_chkfirst = (String)data.get("chkfirst");
        String v_chkfinal = (String)data.get("chkfinal");
        String v_luserid  = (String)data.get("luserid");
        Hashtable v_member = null;

        String v_isb2c      = "N";  // tz_grcode에서 찾아온다.
        String v_ischkfirst = "N";  // tz_subjseq에서 찾아온다.

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        // insert TZ_PROPOSE table
        sql =  "insert into tz_propose ( " ;
        sql +=  " subj,    year,          subjseq,      userid,     ";
        sql +=  " comp,    jik,           appdate,      isdinsert,  ";
        sql +=  " isb2c,   ischkfirst,    chkfirst,     chkfinal,   ";
        sql +=  " proptxt, billstat, ordcode,      cancelkind, ";
        sql +=  " luserid, ldate ) " ;
        sql +=  " values ( ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ? ) ";

        // update TZ_PROPOSE table
        sql1 =  "update tz_propose set cancelkind='', isdinsert=?, chkfirst='Y', chkfinal='Y' " ;
        sql1 +=  " where subj= ? and year=? and subjseq=? and userid =? ";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            pstmt = (PreparedStatement)data.get("propose_pstmt");


            if ( pstmt == null ) { 
                pstmt = connMgr.prepareStatement(sql);
                v_CreatePreparedStatement = true;
            }

            pstmt1 = connMgr.prepareStatement(sql1);
            v_member = getMeberInfo(connMgr, v_userid);

            list = getCourseToSubj(connMgr, v_subj, v_year, v_subjseq);

            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);
                if ( isPropose(connMgr, v_subjseqdata.getSubj(), v_subjseqdata.getYear(), v_subjseqdata.getSubjseq(), v_userid)) { 
                    pstmt1.setString( 1, v_isdinsert);
                    pstmt1.setString( 2, v_subjseqdata.getSubj() );
                    pstmt1.setString( 3, v_subjseqdata.getYear() );
                    pstmt1.setString( 4, v_subjseqdata.getSubjseq() );
                    pstmt1.setString( 5, v_userid);
                    isOk = pstmt1.executeUpdate();
                    continue;  // 조건문이 성립되면 이하 실행은 되지 않고 반복문으로 돌아갑니다.(기존 propose 테이블에 존재할시 Update)
                }

                pstmt.setString( 1, v_subjseqdata.getSubj() );
                pstmt.setString( 2, v_subjseqdata.getYear() );
                pstmt.setString( 3, v_subjseqdata.getSubjseq() );
                pstmt.setString( 4, v_userid);
                pstmt.setString( 5, (String)v_member.get("comp") );
                pstmt.setString( 6, (String)v_member.get("jikwi") );
                pstmt.setString( 7, FormatDate.getDate("yyyyMMddHHmmss") );  // appdate
                pstmt.setString( 8, v_isdinsert );
                pstmt.setString( 9, v_isb2c     );
                pstmt.setString(10, v_ischkfirst);
                pstmt.setString(11, v_chkfirst  );
                pstmt.setString(12, v_chkfinal  );
                pstmt.setString(13, "");   // v_proptxt
                pstmt.setString(14, "");   // v_billstat
                pstmt.setString(15, "");   // v_ordcode
                pstmt.setString(16, "");   // v_cancelkind
                pstmt.setString(17, v_luserid);
                pstmt.setString(18, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate

                isOk = pstmt.executeUpdate();

            }
            
      //--s---20100715 대상자 추가시 결재수단을 무통장으로 추가해준다. 
            if(isOk > 0){
	            //밀리세컨드까지를 아이디로 본다..
				sql2 = "SELECT TO_CHAR(SYSTIMESTAMP,'YYYYMMDDHH24MISSFF') AS ORDER_ID FROM DUAL ";
				ls1 = connMgr.executeQuery(sql2);
			
				if( ls1.next() ) { 
					order_id = ls1.getString("order_id");
				}
            }
           
            //정상 저장되면 결제정보에 넣어준다.
    		if(isOk > 0){
    			PreparedStatement   pstmt2               = null;
    			String sql3 =" insert into pa_payment(order_id,userid,leccode,lecnumb,type,auth_date,year,enterance_dt, amount)";
    			       sql3+=" values(?,?,?,?,?,to_char(sysdate,'yyyymmddhh24miss'),to_char(sysdate,'yyyy'),?,?)";
    			       
    			       pstmt2   = connMgr.prepareStatement(sql3);   
    			       int idx = 1;
    			       pstmt2.setString(idx++,order_id);//주문번호
    			       pstmt2.setString(idx++,v_userid);//회원아이디
    			       pstmt2.setString(idx++,v_subj);//강좌코드
    			       pstmt2.setString(idx++,v_subjseq);//차수
    			       pstmt2.setString(idx++,"PB");//결제종류
    			       pstmt2.setString(idx++,"");//입금예정일
    			       pstmt2.setString(idx++,"");//금액
    			       isOk = pstmt2.executeUpdate();
    			     //  System.out.println("sql3 + isOk :::> "+sql3);
    			     //  System.out.println("sql3 + isOk :::> "+isOk);
    		}
    		
    		//주문번호를 신청 테이블에 넣는다. 
    		if(isOk > 0){
    			PreparedStatement   pstmt2               = null;
    			String sql4 =" update tz_propose set order_id = ? , gubun = ? ";
    				   sql4+=" where subj = ? ";
    				   sql4+=" and year = ? ";
    				   sql4+=" and userid = ? ";
    				   sql4+=" and subjseq = ? ";
    						       
    			       pstmt2   = connMgr.prepareStatement(sql4);   
    			       int idx = 1;
    			       pstmt2.setString(idx++,order_id);//주문번호
    			       pstmt2.setString(idx++,"PB");//결제수단
    			       pstmt2.setString(idx++,v_subj);//강좌코드
    			       pstmt2.setString(idx++,v_year);//회원아이디
    			       pstmt2.setString(idx++,v_userid);//회원아이디
    			       pstmt2.setString(idx++,v_subjseq);//차수
    			       isOk = pstmt2.executeUpdate();
    			      // System.out.println("sql4 + isOk :::> "+sql4);
    			      // System.out.println("sql4 + isOk :::> "+isOk);
    		}
    //--e---20100715 대상자 추가시 결재수단을 무통장으로 추가해준다. 
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( v_CreatePreparedStatement) { 
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            }
        }

        return isOk;
    }



    public boolean isStudent(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        String v_userid = "";

        try { 
            sql = "select userid ";
            sql += "  from tz_student ";
            sql += " where subj   = " + SQLString.Format(p_subj);
            sql += "   and year   = " + SQLString.Format(p_year);
            sql += "   and subjseq= " + SQLString.Format(p_subjseq);
            sql += "   and userid = " + SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_userid = ls.getString("userid");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_userid.equals(p_userid);
    }

    public boolean isPropose(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        String v_userid = "";

        try { 
            sql = "select userid ";
            sql += "  from tz_propose ";
            sql += " where subj   = " + SQLString.Format(p_subj);
            sql += "   and year   = " + SQLString.Format(p_year);
            sql += "   and subjseq= " + SQLString.Format(p_subjseq);
            sql += "   and userid = " + SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_userid = ls.getString("userid");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_userid.equals(p_userid);
    }


    /**
    수강생 등록
    @param  Hashtable        수강생 정보
    @return isOk    1:insert success, 0:insert fail
    **/
    public int insertStudent(Hashtable data) throws Exception { 
        MSSQL_DBConnectionManager mssql_connMgr = null;
        PreparedStatement pstmt2 = null;
        String sql2 = "";
        int isOk2 = 0;
        String  v_contenttype = "";
        String  v_aesserialno = "";

        ListSet ls3 = null;
        String sql3 = "";

        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_userid = (String)data.get("userid");
        String v_isdinsert = (String)data.get("isdinsert");
        String v_chkfirst = (String)data.get("chkfirst");
        String v_chkfinal = (String)data.get("chkfinal");
        String v_luserid  = (String)data.get("luserid");
        Hashtable v_member = null;

        String v_isb2c    = "N";  // tz_grcode에서 찾아온다.
        // 학습기간 설정
        String v_edustart = "";
        String v_eduend   = "";

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        boolean isMailed = false;

        RequestBox box = null;
        box = (RequestBox)data.get("box");

        // insert TZ_STUDENT table
        sql  ="insert into tz_student ( " ;
        sql +=" subj,        year,     subjseq,     userid,    ";
        sql +=" class,       comp,     isdinsert,   score,     ";
        sql +=" tstep,       mtest,    ftest,       report,    ";
        sql +=" act,         etc1,     etc2,        avtstep,   ";
        sql +=" avmtest,     avftest,  avreport,    avact,     ";
        sql +=" avetc1,      avetc2,   isgraduated, isrestudy, ";
        sql +=" isb2c,       edustart, eduend,      branch,    ";
        sql +=" confirmdate, eduno,    luserid,     ldate,     ";
        sql +=" stustatus )  ";
        sql +=" values ( ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?, ?, ?, ?, ";
        sql +=" ?) ";
        
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            pstmt = (PreparedStatement)data.get("student_pstmt");
            if ( pstmt == null ) { 
                pstmt = connMgr.prepareStatement(sql);
                v_CreatePreparedStatement = true;
            }

            v_member = getMeberInfo(connMgr, v_userid);

            list = getCourseToSubj(connMgr, v_subj, v_year, v_subjseq);
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);
                if ( isStudent(connMgr, v_subjseqdata.getSubj(), v_subjseqdata.getYear(), v_subjseqdata.getSubjseq(), v_userid)) { 
                    isOk = 1;
                    continue;
                }

                pstmt.setString( 1, v_subjseqdata.getSubj() );
                pstmt.setString( 2, v_subjseqdata.getYear() );
                pstmt.setString( 3, v_subjseqdata.getSubjseq() );
                pstmt.setString( 4, v_userid);

                pstmt.setString( 5, "0001");   // v_class
                pstmt.setString( 6, (String)v_member.get("comp") );
                pstmt.setString( 7, "Y");   // v_isdinsert
                pstmt.setDouble( 8, 0);     // v_score

                pstmt.setDouble( 9, 0);     // v_tstep
                pstmt.setDouble(10, 0);     // v_mtest
                pstmt.setDouble(11, 0);     // v_ftest
                pstmt.setDouble(12, 0);     // v_report

                pstmt.setDouble(13, 0);     // v_act
                pstmt.setDouble(14, 0);     // v_etc1
                pstmt.setDouble(15, 0);     // v_etc2
                pstmt.setDouble(16, 0);     // v_avtstep

                pstmt.setDouble(17, 0);     // v_avmtest
                pstmt.setDouble(18, 0);     // v_avftest
                pstmt.setDouble(19, 0);     // v_avreport
                pstmt.setDouble(20, 0);     // v_avact

                pstmt.setDouble(21, 0);     // v_avetc1
                pstmt.setDouble(22, 0);     // v_avetc2
                pstmt.setString(23, "N");   // v_isgraduated
                pstmt.setString(24, "N");   // v_isrestudy)

                pstmt.setString(25, v_isb2c);
                pstmt.setString(26, v_edustart);
                pstmt.setString(27, v_eduend);
                pstmt.setInt(28, 99);  // v_branch

                pstmt.setString(29, "");    // v_confirmdate
                pstmt.setInt   (30, 0);     // v_eduno
                pstmt.setString(31, v_luserid);
                pstmt.setString(32, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate

                pstmt.setString(33, "Y"); // stustatus

                isOk = pstmt.executeUpdate();
    			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

                if (isOk > 0) {
	                // SCORM2004 : 사용자별 SeqInfo 객체 생성
	                if("Y".equals(CodeAdminBean.getIsScorm(v_subj))) {
	                	ScormCourseBean scb = new ScormCourseBean();
		                String [] emp_id = { v_userid };
		                boolean result = scb.processTreeObjectForUsers(v_subjseqdata.getSubj(), v_subjseqdata.getYear(), v_subjseqdata.getSubjseq(), emp_id);
	                }
                }
                // 신청승인 및 입과 메일 보낼 자리
                // isMailed = sendAcceptMail(connMgr, box, v_subj, v_year, v_subjseq, v_userid);
            }
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( v_CreatePreparedStatement) { 
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            }
            if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( mssql_connMgr != null ) { try { mssql_connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    반려시 학습정보 백업
    @param  Hashtable        수강생 정보
    @return isOk            1:insert success, 0:insert fail
    **/
    public int insertStudentreject(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_userid = (String)data.get("userid");
        String v_isdinsert = (String)data.get("isdinsert");
        String v_chkfirst = (String)data.get("chkfirst");
        String v_chkfinal = (String)data.get("chkfinal");
        String v_luserid  = (String)data.get("luserid");
        String v_targettb = (String)data.get("targettb");
        String v_sourcetb = (String)data.get("sourcetb");
        Hashtable v_member = null;

        String v_isb2c    = "N";  // tz_grcode에서 찾아온다.
        // 학습기간 설정
        String v_edustart = "";
        String v_eduend   = "";

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        boolean isMailed = false;

        RequestBox box = null;
        box = (RequestBox)data.get("box");

        // insert TZ_STUDENT table
        sql="insert into " +v_targettb + "                           \n";
        sql +="(                                                  \n";
        sql +="  subj,        year,     subjseq,     userid,      \n";
        sql +="  class,       comp,     isdinsert,   score,       \n";
        sql +="  tstep,       mtest,    ftest,       report,      \n";
        sql +="  act,         etc1,     etc2,        avtstep,     \n";
        sql +="  avmtest,     avftest,  avreport,    avact,       \n";
        sql +="  avetc1,      avetc2,   isgraduated, isrestudy,   \n";
        sql +="  isb2c,       edustart, eduend,      branch,      \n";
        sql +="  confirmdate, eduno,    luserid,     ldate,       \n";
        sql +="  stustatus                                        \n";
        sql +=")                                                  \n";
        sql +="  select                                           \n";
        sql +="    subj,        year,     subjseq,     userid,    \n";
        sql +="    class,       comp,     isdinsert,   score,     \n";
        sql +="    tstep,       mtest,    ftest,       report,    \n";
        sql +="    act,         etc1,     etc2,        avtstep,   \n";
        sql +="    avmtest,     avftest,  avreport,    avact,     \n";
        sql +="    avetc1,      avetc2,   isgraduated, isrestudy, \n";
        sql +="    isb2c,       edustart, eduend,      branch,    \n";
        sql +="    confirmdate, eduno,    luserid,     ldate,     \n";
        sql +="    stustatus                                      \n";
        sql +="  from                                             \n";
        sql +="    " +v_sourcetb + "                                 \n";
        sql +="  where                                            \n";
        sql +="    userid = " + StringManager.makeSQL(v_userid) + " and subj =" + StringManager.makeSQL(v_subj) + " and year = " + StringManager.makeSQL(v_year) + " and subjseq = " + StringManager.makeSQL(v_subjseq) + "\n";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

//            pstmt = (PreparedStatement)data.get("student_pstmt");
//System.out.println("pstmt : " + pstmt);
//            if ( pstmt == null ) { 
//                v_CreatePreparedStatement = true;
//                pstmt = connMgr.prepareStatement(sql);
//            }
//System.out.println("insertStudentreject sql : " + sql);
            isOk = connMgr.executeUpdate(sql);
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

            if (isOk > 0 && "tz_student".equals(v_targettb)) {
                // SCORM2004 : 사용자별 SeqInfo 객체 생성
                if("Y".equals(CodeAdminBean.getIsScorm(v_subj))) {
                	ScormCourseBean scb = new ScormCourseBean();
	                String [] emp_id = { v_userid };
	                boolean result = scb.processTreeObjectForUsers(v_subj, v_year, v_subjseq, emp_id);
                }
            }
            
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( v_CreatePreparedStatement) { 
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            }
        }

        return isOk;
    }



    public boolean  sendAcceptMail(DBConnectionManager connMgr, RequestBox box, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        boolean isMailed = false;
        String v_mailContent = "";

        String  v_toCono   = "";
        String  v_toEmail  = "";
        String  v_ismailing= "";
        String  v_toname   = ""; // 님이 tz_member

        String  v_isonoff  = ""; // tz_subj + tz_subjseq
        String  v_subjnm   = "";
        String  v_grseqnm  = ""; // ◈ 교육기수명
        String  v_edustrart= "";
        String  v_eduend   = ""; // ◈ 교육기간
        int     v_edudays  = 0;  // ◈ 교육일수
        String  v_place     = ""; // ◈ 교육장소
        int  v_gradstep     = 0;     // ◈ 수료기준 진도율
        int  v_wgradstep    = 0;    // ◈ 수료기준 진도율 가중치
        int  v_wreport      = 0;     // ◈ 과제
        int  v_wmtest       = 0;     // ◈ 중간평가
        int  v_wftest       = 0;     // ◈ 최종평가
        int  v_wact         = 0;     // ◈ Activity
        int  v_gradscore    = 0;     // 총 수료점수 점
        int  v_biyong       = 0;     // 수강료

        int     v_reportcnt= 0;  //    과제 회
        int     v_testcnt  = 0;  //    평가 회
        int     v_actcnt   = 0;  //    평가 회


        ListSet ls  = null;
        String  sql = "";

        String  v_sql01 = "";
        v_sql01 += "select userid as cono, email_id as email, name, email_get as ismailing ";
        v_sql01 += "  from tz_member ";
        v_sql01 += " where userid = " + SQLString.Format(p_userid);

        String  v_sql02 = "";
        v_sql02  = "select c.codenm, b.subjnm, b.edustart, b.eduend, a.edudays, a.place, ";
        v_sql02 += "       b.gradstep,b.wstep, b.wreport, b.wmtest, b.wftest, b.wact, b.gradscore, b.biyong, ";
        v_sql02 += "       (select grseqnm from tz_grseq where grcode = b.grcode and gyear = b.gyear and grseq=b.grseq) grseqnm ";
        v_sql02 += "  from tz_subj    a,  ";
        v_sql02 += "       tz_subjseq b,  ";
        v_sql02 += "       tz_code    c   ";
        v_sql02 += " where a.subj    = b.subj  ";
        v_sql02 += "   and a.isonoff = c.code  ";
        v_sql02 += "   and c.gubun   = '0004'  ";
        v_sql02 += "   and b.subj    = " + SQLString.Format(p_subj);
        v_sql02 += "   and b.year    = " + SQLString.Format(p_year);
        v_sql02 += "   and b.subjseq = " + SQLString.Format(p_subjseq);

        String  v_sql03 = "";
        v_sql03 = "select count(*) testcnt  ";
        v_sql03 += "  from tz_exampaper  ";
        v_sql03 += " where subj    = " + SQLString.Format(p_subj);
        v_sql03 += "   and year    = " + SQLString.Format(p_year);
        v_sql03 += "   and subjseq = " + SQLString.Format(p_subjseq);

        String  v_sql04 = "";
        v_sql04 = " select count(*) reportcnt";
        v_sql04 += "   from tz_projord ";
        v_sql04 += "  where subj    = " + SQLString.Format(p_subj);
        v_sql04 += "    and year    = " + SQLString.Format(p_year);
        v_sql04 += "    and subjseq = " + SQLString.Format(p_subjseq);

        String  v_sql05 = "";
        v_sql05 = "select count(*) actcnt ";
        v_sql05 += "  from tz_activity  ";
        v_sql05 += " where subj = " + SQLString.Format(p_subj);

        try { 
//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            String v_sendhtml = "mail1.html";
            FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
            MailSet mset = new MailSet(box);        //      메일 세팅 및 발송
            String v_mailTitle = "안녕하세요? e-Eureka 입니다.(신청승인안내)";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /
            mset.setSender(fmail);     //  메일보내는 사람 세팅

            sql = v_sql01;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_toCono  = ls.getString("cono");
                v_toEmail = ls.getString("email");
                v_toname  = ls.getString("name");
                v_ismailing = ls.getString("ismailing");
            }

            sql = v_sql02;
            ls.close();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_isonoff   = ls.getString("codenm");
                v_subjnm    = ls.getString("subjnm");
                v_grseqnm   = ls.getString("grseqnm");
                v_edustrart = ls.getString("edustart");
                v_eduend    = ls.getString("eduend");
                v_edudays   = ls.getInt("edudays");
                v_place     = ls.getString("place");

                if ( v_place.equals(""))      { 
                    v_place = "사이버교육";
                }

                v_gradstep  = ls.getInt("gradstep");
                v_wgradstep = ls.getInt("wstep");
                v_wreport   = ls.getInt("wreport");
                v_wmtest    = ls.getInt("wmtest");
                v_wftest    = ls.getInt("wftest");
                v_wact      = ls.getInt("wact");
                v_gradscore = ls.getInt("gradscore");
                v_biyong    = ls.getInt("biyong");
            }

            sql = v_sql03;
            ls.close();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_testcnt  = ls.getInt("testcnt");
            }

            sql = v_sql04;
            ls.close();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_reportcnt  = ls.getInt("reportcnt");
            }

            sql = v_sql05;
            ls.close();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_actcnt  = ls.getInt("actcnt");
            }

            fmail.setVariable("toname",   v_toname);
            fmail.setVariable("isonoff",  v_isonoff);
            fmail.setVariable("subjnm",   v_subjnm);
            fmail.setVariable("grseqnm",  v_grseqnm);
            fmail.setVariable("edustart", FormatDate.getFormatDate(v_edustrart, "yyyy.MM.dd") );
            fmail.setVariable("eduend",   FormatDate.getFormatDate(v_eduend, "yyyy.MM.dd") );
            fmail.setVariable("edudays",  String.valueOf(v_edudays));
            fmail.setVariable("place",    v_place);

            fmail.setVariable("gradstep",   String.valueOf(v_gradstep));
            fmail.setVariable("wgradstep",  String.valueOf(v_wgradstep));
            fmail.setVariable("wreport",    String.valueOf(v_wreport));
            fmail.setVariable("wmtest",     String.valueOf(v_wmtest));
            fmail.setVariable("wftest",     String.valueOf(v_wftest));
            fmail.setVariable("wact",       String.valueOf(v_wact));
            fmail.setVariable("gradscore",  String.valueOf(v_gradscore));
            fmail.setVariable("biyong",     String.valueOf(v_biyong));

            fmail.setVariable("reportcnt", String.valueOf(v_reportcnt));
            fmail.setVariable("testcnt",   String.valueOf(v_testcnt));
            fmail.setVariable("actcnt",    String.valueOf(v_actcnt));

            v_mailContent = fmail.getNewMailContent();

            //isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml); TODO : 09.10.27 
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isMailed;
    }


    public ArrayList getCourseToSubj(String p_subj, String p_year, String p_subjseq) throws Exception { 
        DBConnectionManager connMgr = null;
        ArrayList           list    = null;

        try { 
            connMgr = new DBConnectionManager();
            list    = getCourseToSubj(connMgr, p_subj, p_year, p_subjseq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list;
    }

    
    public ArrayList getCourseToSubj(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        SubjseqData     data    = null;
        ListSet         ls      = null;
        ArrayList       list    = new ArrayList();
        StringBuffer    sbSQL   = new StringBuffer("");
        
        int             iSysAdd = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String          temp    = ""; 
        

        try { 
            temp                = GetCodenm.get_upperclass(p_subj, p_year, p_subjseq);
            
            if ( temp.equals("COUR") ) { 
                sbSQL.append(" select  grcode                                               \n")
                     .append("     ,   gyear                                                \n")
                     .append("     ,   grseq                                                \n")
                     .append("     ,   subj                                                 \n")
                     .append("     ,   year                                                 \n")
                     .append("     ,   subjseq                                              \n")
                     .append(" from    tz_subjseq                                           \n")
                     .append(" where   course      = " + SQLString.Format(p_subj   ) + "    \n")
                     .append(" and     cyear       = " + SQLString.Format(p_year   ) + "    \n")
                     .append(" and     courseseq   = " + SQLString.Format(p_subjseq) + "    \n");
                     
                //System.out.println(this.getClass().getName() + "." + "getCourseToSubj() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                ls  = connMgr.executeQuery(sbSQL.toString());
                
                while ( ls.next() ) { 
                    data    = new SubjseqData();
                    
                    data.setGrcode ( ls.getString("grcode"  ) );
                    data.setGyear  ( ls.getString("gyear"   ) );
                    data.setGrseq  ( ls.getString("grseq"   ) );
                    data.setSubj   ( ls.getString("subj"    ) );
                    data.setYear   ( ls.getString("year"    ) );
                    data.setSubjseq( ls.getString("subjseq" ) );
                    
                    list.add(data);
                }
            } else { 
                data    = new SubjseqData();
//System.out.println("<<<< 여기");
                data.setSubj    (p_subj     );
                data.setYear    (p_year     );
                data.setSubjseq (p_subjseq  );
                
                list.add(data);
            }
//System.out.println("ProposeBean.getCourseToSubj"+connMgr);
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();
                } catch ( Exception e ) { } 
            }
        }
        
        return list;
    }

    
    public String getSubjYear(String p_grcode, String p_gyear, String p_grseq, String p_subj, String p_subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String v_year = "";
        try { 
            connMgr = new DBConnectionManager();
            v_year = getSubjYear(connMgr, p_grcode, p_gyear, p_grseq, p_subj, p_subjseq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_year;
    }

    public String getSubjYear(DBConnectionManager connMgr, String p_grcode, String p_gyear, String p_grseq, String p_subj, String p_subjseq) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        String v_year = "";
        try { 
            sql = "select year ";
            sql += "  from tz_subjseq ";
            sql += " where subj   = " + SQLString.Format(p_subj);
            sql += "   and subjseq= " + SQLString.Format(p_subjseq);
            sql += "   and grcode = " + SQLString.Format(p_grcode);
            sql += "   and gyear  = " + SQLString.Format(p_gyear);
            // sql += "   and grseq  = " + SQLString.Format(p_grseq);
            sql += " union all ";
            sql += "select cyear ";
            sql += "  from tz_courseseq ";
            sql += " where course   = " + SQLString.Format(p_subj);
            sql += "   and courseseq= " + SQLString.Format(p_subjseq);
            sql += "   and grcode   = " + SQLString.Format(p_grcode);
            sql += "   and gyear    = " + SQLString.Format(p_gyear);
            // sql += "   and grseq    = " + SQLString.Format(p_grseq);

//System.out.println(" getSubjYear sql == > " + sql );


            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_year = ls.getString("year");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_year;
    }

    public String getProposeApproval(String p_subj, String p_year, String p_subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String v_proposeapp = "";
        try { 
            connMgr = new DBConnectionManager();
            v_proposeapp = getProposeApproval(connMgr, p_subj, p_year, p_subjseq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_proposeapp;
    }

    public String getProposeApproval(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String v_proposeapp = "";

        String sql  = "";

        try { 
            sql  = "select useproposeapproval from tz_subjseq ";
            sql += " where subj = " + SQLString.Format(p_subj);
            sql += " and year = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            //System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_proposeapp = ls.getString("useproposeapproval");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_proposeapp;
    }

    public String getManagerApproval(String p_subj, String p_year, String p_subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String v_managerapp = "";
        try { 
            connMgr = new DBConnectionManager();
            v_managerapp = getManagerApproval(connMgr, p_subj, p_year, p_subjseq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_managerapp;
    }

    public String getManagerApproval(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String v_managerapp = "";

        String sql  = "";

        try { 
            sql  = "select usemanagerapproval from tz_subjseq ";
            sql += " where subj = " + SQLString.Format(p_subj);
            sql += " and year = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_managerapp = ls.getString("usemanagerapproval");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_managerapp;
    }
    

    public Hashtable getMeberInfo(String p_userid) throws Exception { 
        Hashtable member = null;
        DBConnectionManager	connMgr	= null;

        try { 
            connMgr = new DBConnectionManager();
            member = getMeberInfo(connMgr, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return member;
    }

    public Hashtable getMeberInfo(DBConnectionManager connMgr, String p_userid) throws Exception { 
        Hashtable member = new Hashtable();
        ListSet             ls      = null;
        String sql  = "";

        try {
/* 2006.06.15 - 수정            
            sql = " select a.comp, a.jikup, a.cono, a.birth_date,  a.userid, a.name,  a.email,  a.comptel,                          \n";
            sql += "        a.jikwi, get_jikwinm(jikwi,comp) jikwinm, get_compnm(comp,2,2) compnm, trim(a.deptnam) deptnam,";
            sql += "        a.office_gbn, a.work_plc, a.work_plcnm ";
            sql += "   from tz_member a                         ";
            sql += " where a.userid = '" +p_userid + "'            ";
*/
            sql = "select a.comp,                   			\n" +
                  "    	  a.lvl_nm jikup,      					\n" +
                  "    	  fn_crypt('2', a.birth_date, 'knise') birth_date,               				\n" +
                  "       a.userid,                             \n" +
                  "       a.name,                               \n" +
                  "       a.email,                   			\n" +
                  "       b.telno as comptel                  	\n" +
                  "from   tz_member a                      		\n" +
                  "     , tz_compclass b                      	\n" +
                  "where  a.comp = b.comp(+)					\n" +
                  "and    a.userid = " + StringManager.makeSQL(p_userid) + "           \n";
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                member.put("comp",  ls.getString("comp") );
                member.put("jikup", ls.getString("jikup") );
                member.put("birth_date", ls.getString("birth_date") );
                member.put("name",  ls.getString("name") );
                member.put("email", ls.getString("email") );
                member.put("comptel",   ls.getString("comptel") );
            }
//System.out.println("ProposeBean.getMeberInfo");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return member;
    }

    public ArrayList SelectCancelTargetMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox             dbox    = null;

        String sql  = "";
        String v_searchtext = box.getString("p_searchtext");

        try { 
            list = new ArrayList();
            if ( !v_searchtext.equals("") ) { 
/* 2006.06.15 - 수정
                sql = "select a.grcode, a.gyear, a.grseq, a.subj, a.year, a.subjseq, a.course, a.cyear, a.courseseq, a.useproposeapproval,  ";
                sql += "       a.subjnm, a.propstart, a.propend, a.edustart, a.eduend,  get_jikwinm(d.jikwi,d.comp) jikwi,";
                sql += "       d.comp asgn, get_compnm(d.comp,2,4) asgnnm, d.jikup, get_jikupnm(d.jikup, d.comp,d.jikupnm) jikupnm, d.userid, d.cono, d.name, ";
                sql += "       c.appdate, c.chkfirst, c.chkfinal, c.isproposeapproval, b.coursenm, ";
                sql += "      (select grcodenm from tz_grcode where grcode = a.grcode) grcodenm, ";
                sql += "      (select grseqnm from tz_grseq where grcode = a.grcode and gyear = a.gyear and grseq = a.grseq) grseqnm ";
                sql += "  from tz_subjseq   a, ";
                sql += "       tz_courseseq b, ";
                sql += "       tz_propose   c, ";
                sql += "       tz_member    d  ";
                sql += " where a.subj    = c.subj ";
                sql += "   and a.year    = c.year ";
                sql += "   and a.subjseq = c.subjseq ";
                sql += "   and a.course  = b.course  " ;
                sql += "   and a.cyear   = b.cyear   ";
                sql += "   and a.courseseq = b.courseseq ";
                sql += "   and c.userid  = d.userid  ";
                sql += "   and c.cancelkind  is null";
                sql += "   and (d.userid like " + SQLString.Format("%" +v_searchtext + "%") + " or d.name like " + SQLString.Format("%" +v_searchtext + "%") + ")";
                sql += " order by d.name, d.userid, a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
*/

                
                sql = "select                                                                            \n" +
                      "    a.grcode,                                                                     \n" +
                      "    a.gyear,                                                                      \n" +
                      "    a.grseq,                                                                      \n" +
                      "    a.subj,                                                                       \n" +
                      "    a.year,                                                                       \n" +
                      "    a.subjseq,                                                                    \n" +
                      "    a.course,                                                                     \n" +
                      "    a.cyear,                                                                      \n" +
                      "    a.courseseq,                                                                  \n" +
                      "    a.useproposeapproval,                                                         \n" +
                      "    a.subjnm,                                                                     \n" +
                      "    a.propstart,                                                                  \n" +
                      "    a.propend,                                                                    \n" +
                      "    a.edustart,                                                                   \n" +
                      "    a.eduend,                                                                     \n" +
                      "    b.coursenm,                                                                   \n" +
                      "    c.appdate,                                                                    \n" +
                      "    c.chkfirst,                                                                   \n" +
                      "    c.chkfinal,                                                                   \n" +
                      "    c.isproposeapproval,                                                          \n" +
                      "    d.group_id as asgn,									                         \n" +
                      "    d.post_nm as jikup,                                                           \n" +
                      "    d.userid,                                                                     \n" +
                      "    d.name,                                                                       \n" +
                      "    (select                                                                       \n" +
                      "         grcodenm                                                                 \n" +
                      "     from                                                                         \n" +
                      "         tz_grcode                                                                \n" +
                      "     where                                                                        \n" +
                      "         grcode = a.grcode                                                        \n" +
                      "    ) as grcodenm,                       										 \n" +
                      "    (select                                                                       \n" +
                      "         grseqnm                                                                  \n" +
                      "     from                                                                         \n" +
                      "         tz_grseq                                                                 \n" +
                      "     where                                                                        \n" +
                      "             grcode = a.grcode                                                    \n" +
                      "         and gyear  = a.gyear                                                     \n" +
                      "         and grseq  = a.grseq                                                     \n" +
                      "    ) as grseqnm                         									     \n" +
                      "from                                                                              \n" +
                      "    tz_subjseq                                    a,                              \n" +
                      "    tz_courseseq                                  b,                              \n" +
                      "    tz_propose                                    c,                              \n" +
                      "    tz_member                                     d                               \n" +
                      "where                                                                             \n" +
                      "        a.subj            = c.subj                                                \n" +
                      "    and a.year            = c.year                                                \n" +
                      "    and a.subjseq         = c.subjseq                                             \n" +
                      "    and a.course          = b.course                                              \n" +
                      "    and a.cyear           = b.cyear                                               \n" +
                      "    and a.courseseq       = b.courseseq                                           \n" +
                      "    and c.userid          = d.userid                                              \n" +
                      "    and c.cancelkind     is null                                                  \n" +
                      "    and (     d.userid like " + SQLString.Format("%" +v_searchtext + "%") + "     \n" +
                      "          or  d.name   like " + SQLString.Format("%" +v_searchtext + "%") + "     \n" +
                      "        )                                                                         \n" +
                      "order by                                                                          \n" +
                      "    d.name,                                                                       \n" +
                      "    d.userid,                                                                     \n" +
                      "    a.course,                                                                     \n" +
                      "    a.cyear,                                                                      \n" +
                      "    a.courseseq,                                                                  \n" +
                      "    a.subj,                                                                       \n" +
                      "    a.year,                                                                       \n" +
                      "    a.subjseq                                                                     \n";
                
                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                    list.add(dbox);
                }

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
    직접입과 취소처리
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int Cancel(RequestBox box) throws Exception { 
        // 취소권한 체크
        DBConnectionManager	connMgr	= null;
        int isOk = 0;

        String v_subj     = "";
        String v_year     = "";
        String v_subjseq  = "";
        String v_userid   = "";
        int   v_seq       = 0;
        String v_luserid  = box.getSession("userid");

        // p_checks 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_checks = box.getVector("p_checks");
        Enumeration em  = v_checks.elements();
        StringTokenizer v_token = null;
        String v_tempStr = "";
        int i = 0;
        Hashtable insertData = new Hashtable();
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            while ( em.hasMoreElements() ) { 
                insertData.clear();
                insertData.put("connMgr",  connMgr);

                i = 0;
                v_token = new StringTokenizer((String)em.nextElement(), ";");
                while ( v_token.hasMoreTokens() ) { 
                    v_tempStr = v_token.nextToken();
                    switch (i) { 
                        case 0:
                            insertData.put("subj", v_tempStr);
                            break;
                        case 1:
                            insertData.put("year", v_tempStr);
                            break;
                        case 2:
                            insertData.put("subjseq", v_tempStr);
                            break;
                        case 3:
                            insertData.put("userid", v_tempStr);
                            break;
                    }
                    i++;
                }
                insertData.put("cancelkind", box.getString("p_cancelkind") );
                insertData.put("reason", box.getString("p_reason") );
                insertData.put("luserid", box.getSession("userid") );

                isOk = insertCancel(insertData);
                isOk = deletePropose(insertData);
                isOk = deleteStudent(insertData);
            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, "Propose.Cancel");
            throw new Exception("Propose.Cancel" + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    직접입과 취소처리
    @param Hashtable  취소관련정보
    @return isOk      1:insert success,0:insert fail
    */
    public int insertCancel(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj       = (String)data.get("subj");
        String v_year       = (String)data.get("year");
        String v_subjseq    = (String)data.get("subjseq");
        String v_userid     = (String)data.get("userid");
        String v_cancelkind = (String)data.get("cancelkind");
        String v_reason     = (String)data.get("reason");
        String v_luserid    = (String)data.get("luserid");
        String v_reasoncd     = (String)data.get("reasoncd");
        int v_seq = 0;
//System.out.println("@@@@@@@ ===== 반려자 취소 데이터 넣기 : "+v_userid+"/"+v_year+"/"+v_subj+"/"+v_subjseq);
        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        // insert TZ_CANCEL table
        sql =  "insert into TZ_CANCEL ( " ;
        sql +=  " subj,    year,          subjseq,      userid,  ";
        sql +=  " seq,     cancelkind,    canceldate,   reason,  ";
        sql +=  " luserid, ldate, reasoncd )  ";
        sql +=  " values ( ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ?, ?) ";
//System.out.println("@===1");
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
//System.out.println("@@@@@@@ 트렌젝션 없음 : "+v_userid+"/"+v_year+"/"+v_subj+"/"+v_subjseq);
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);
//System.out.println("@===2");
            list = getCourseToSubj(connMgr, v_subj, v_year, v_subjseq);
//System.out.println("@===3"+list.size());

            for ( int i = 0; i<list.size(); i++ ) { 
            	
            	
//System.out.println("@@@@@@@ ===== for : "+v_userid+"/"+v_subjseqdata.getYear()+"/"+v_subjseqdata.getSubj()+"/"+v_subjseqdata.getSubjseq());            	
                v_subjseqdata = (SubjseqData)list.get(i);
                Hashtable maxdata = new Hashtable();
                maxdata.clear();
                maxdata.put("seqcolumn","seq");
                maxdata.put("seqtable","tz_cancel");
                maxdata.put("paramcnt","4");
                maxdata.put("param0","subj");
                maxdata.put("subj",   SQLString.Format(v_subjseqdata.getSubj() ));
                maxdata.put("param1","year");
                maxdata.put("year",   SQLString.Format(v_subjseqdata.getYear() ));
                maxdata.put("param2","subjseq");
                maxdata.put("subjseq",SQLString.Format(v_subjseqdata.getSubjseq() ));
                maxdata.put("param3","userid");
                maxdata.put("userid", SQLString.Format(v_userid));
                v_seq = SelectionUtil.getSeq(maxdata);

                pstmt.setString( 1, v_subjseqdata.getSubj() );
                pstmt.setString( 2, v_subjseqdata.getYear() );
                pstmt.setString( 3, v_subjseqdata.getSubjseq() );
                pstmt.setString( 4, v_userid);
                pstmt.setInt   ( 5, v_seq);
                pstmt.setString( 6, v_cancelkind);
                pstmt.setString( 7, FormatDate.getDate("yyyyMMddHHmmss") ); // canceldata
                pstmt.setString( 8, v_reason);
                pstmt.setString( 9, v_luserid);
                pstmt.setString(10, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate
                pstmt.setString(11, v_reasoncd); // ldate
                isOk = pstmt.executeUpdate();
            }

//System.out.println("{}{}{}");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


    /**
    수강신청정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deletePropose(Hashtable data) throws Exception { 
        boolean             v_CreateConnManager = false;
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ArrayList           list                = null;
        SubjseqData         v_subjseqdata       = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 0;
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_subj              = (String)data.get("subj"   );
        String              v_year              = (String)data.get("year"   );
        String              v_subjseq           = (String)data.get("subjseq");
        String              v_userid            = (String)data.get("userid" );
        
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                v_CreateConnManager = true;
                connMgr             = new DBConnectionManager();
                connMgr.setAutoCommit(false);
            }

            sbSQL.append(" DELETE FROM Tz_Propose   \n")
                 .append(" WHERE   subj    = ?      \n")
                 .append(" AND     year    = ?      \n")
                 .append(" AND     subjseq = ?      \n")
                 .append(" AND     userid  = ?      \n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            list    = getCourseToSubj(v_subj, v_year, v_subjseq);

            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata   = (SubjseqData)list.get(i);

                pstmt.setString(1, v_subjseqdata.getSubj()      );
                pstmt.setString(2, v_subjseqdata.getYear()      );
                pstmt.setString(3, v_subjseqdata.getSubjseq()   );
                pstmt.setString(4, v_userid                     );
                
                isOk            = pstmt.executeUpdate();
            }
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }        
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { } 
            }
        
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }


    /**
    대상자정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteEduTarget(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_grcode = (String)data.get("grcode");
        String v_subjcourse = (String)data.get("subjcourse");
        String v_mastercd = (String)data.get("mastercd");
        String v_gyear = (String)data.get("gyear");
        // String v_subjseq = (String)data.get("subjseq");
        String v_userid = (String)data.get("userid");


        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            sql = "delete from TZ_EDUTARGET where mastercd = ? and userid = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_mastercd);
            pstmt.setString(2, v_userid);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    
    /**
    학습정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteStudent(Hashtable data) throws Exception { 
        /*
        // 수강신청정보 삭제
        insert into tz_propose_canceled  select * from tz_propose
        where subj=p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        delete tz_propose
         where subj=p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        // 교육생정보 삭제
        insert into tz_student_canceled  select * from tz_student
        where subj=p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        delete from tz_student
         where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        // 과거교육생정보 삭제
        insert into tz_stold_canceled  select * from tz_stold
        where subj=p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        delete from tz_stold
         where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        // 진도정보 삭제
        insert into tz_progress_canceled  select * from tz_progress
        where subj=p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        insert into tz_progressobj_canceled  select * from tz_progressobj
        where subj=p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        delete from tz_progress
         where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        delete from tz_progressobj
         where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        // 평가결과정보 삭제
        insert into tz_examresult_canceled  select * from tz_examresult
        where subj=p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        delete from tz_examresult
         where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        // Activity제출정보 삭제
        for x in c2 loop
            insert into tz_activity_ans_canceled
            values(x.SUBJ,x.YEAR,x.SUBJSEQ,x.DATES,x.SEQ,x.birth_date,x.INDATE,x.CONTENT,x.POINT,x.POINT2,x.BRANCH,x.Lbirth_date,x.LDATE);
        end loop
            commit;
        delete from tz_activity_ans
         where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        // 프로젝트정보 삭제
        for x in c1 loop
            insert into tz_projrep_canceled(subj,year,subjseq,ordseq,projgrp,seq,title,
                contents,f_open,f_state,grade,upfile,tutitle,
                tucontents,tuupfile,indate,lbirth_date,ldate,dates,grade_tmp,grade_mas,tudate)
            values(x.subj,x.year,x.subjseq,x.ordseq,x.projgrp,x.seq,x.title,
                x.contents,x.f_open,x.f_state,x.grade,x.upfile,x.tutitle,
                x.tucontents,x.tuupfile,x.indate,x.lbirth_date,x.ldate,x.dates,x.grade_tmp,x.grade_mas,x.tudate);
        end loop
            commit;
        delete from tz_projrep
         where subj = p_subj and year=p_year and subjseq=p_subjseq and projgrp=p_birth_date;
        delete from tz_projgrp
         where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        // COP type의 과제에 대한 조원들의 평가결과 삭제
        insert into tz_coprep_canceled  select * from tz_coprep where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        delete from tz_coprep where subj = p_subj and year=p_year and subjseq=p_subjseq and birth_date=p_birth_date;
        */

        boolean             v_CreateConnManager = false;
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ArrayList           list                = null;
        SubjseqData         v_subjseqdata       = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 1;
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_subj              = (String)data.get("subj"   );
        String              v_year              = (String)data.get("year"   );
        String              v_subjseq           = (String)data.get("subjseq");
        String              v_userid            = (String)data.get("userid" );

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                v_CreateConnManager = true;

                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);
            }

            sbSQL.append(" delete from tz_student   \n")
                 .append(" where   subj    = ?      \n")
                 .append(" and     year    = ?      \n")
                 .append(" and     subjseq = ?      \n")
                 .append(" and     userid  = ?      \n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            list    = getCourseToSubj(v_subj, v_year, v_subjseq);
            
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata   = (SubjseqData)list.get(i);

                pstmt.setString(1, v_subjseqdata.getSubj()      );
                pstmt.setString(2, v_subjseqdata.getYear()      );
                pstmt.setString(3, v_subjseqdata.getSubjseq()   );
                pstmt.setString(4, v_userid                     );

                pstmt.executeUpdate();

                // SCORM2004 : 사용자별 SeqInfo 객체 생성
                if("Y".equals(CodeAdminBean.getIsScorm(v_subj))) {
                	ScormCourseBean scb = new ScormCourseBean();
	                String [] emp_id = { v_userid };
	                boolean result = scb.deleteTreeObjectForUsers(v_subjseqdata.getSubj(), v_subjseqdata.getYear(), v_subjseqdata.getSubjseq(), emp_id);
                }
            }
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }        
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { } 
            }
        
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }


    /**
    학습자반려정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteStudentreject(Hashtable data) throws Exception { 

        int isOk = 1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_userid = (String)data.get("userid");

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            sql = "delete from tz_studentreject where subj = ? and year = ? and subjseq = ? and userid = ? ";
            pstmt = connMgr.prepareStatement(sql);

            list = getCourseToSubj(v_subj, v_year, v_subjseq);
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);

                pstmt.setString(1, v_subjseqdata.getSubj() );
                pstmt.setString(2, v_subjseqdata.getYear() );
                pstmt.setString(3, v_subjseqdata.getSubjseq() );
                pstmt.setString(4, v_userid);

                pstmt.executeUpdate();
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    /**
    학습자반려테이블 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteCancel(Hashtable data) throws Exception { 

        int isOk = 1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_userid = (String)data.get("userid");

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            sql = "delete from TZ_CANCEL where subj = ? and year = ? and subjseq = ? and userid = ? ";
            pstmt = connMgr.prepareStatement(sql);

            list = getCourseToSubj(v_subj, v_year, v_subjseq);
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);

                pstmt.setString(1, v_subjseqdata.getSubj() );
                pstmt.setString(2, v_subjseqdata.getYear() );
                pstmt.setString(3, v_subjseqdata.getSubjseq() );
                pstmt.setString(4, v_userid);

                pstmt.executeUpdate();
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


     /**
    학습정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteStold(Hashtable data) throws Exception { 
        int isOk = 1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj    = (String)data.get("subj");
        String v_year    = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_userid  = (String)data.get("userid");

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            sql = "delete from TZ_STOLD where subj = ? and year = ? and subjseq = ? and userid = ? ";
            pstmt = connMgr.prepareStatement(sql);

            list = getCourseToSubj(v_subj, v_year, v_subjseq);
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);

                pstmt.setString(1, v_subjseqdata.getSubj() );
                pstmt.setString(2, v_subjseqdata.getYear() );
                pstmt.setString(3, v_subjseqdata.getSubjseq() );
                pstmt.setString(4, v_userid);

                pstmt.executeUpdate();
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


    public int updatePropose(Hashtable data) throws Exception { 
        DBConnectionManager     connMgr             = null;
        //PreparedStatement   pstmt               = null;
        ArrayList               list                = null;
        SubjseqData             v_subjseqdata       = null;
        StringBuffer            sbSQL               = new StringBuffer("");
        
        int                     iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        int                     isOk                = 0;
        boolean                 v_CreateConnManager = false;
        

        String                  v_subj              = (String)data.get("subj"               );
        String                  v_year              = (String)data.get("year"               );
        String                  v_subjseq           = (String)data.get("subjseq"            );
        String                  v_appsubjseq        = (String)data.get("appsubjseq"         );

        String                  v_userid            = (String)data.get("userid"             );
        String                  v_isdinsert         = (String)data.get("isdinsert"          );
        String                  v_chkfirst          = (String)data.get("chkfirst"           );
        String                  v_chkfinal          = (String)data.get("chkfinal"           );
        String                  v_isproposeapproval = (String)data.get("isproposeapproval"  );
        String                  v_luserid           = (String)data.get("luserid"            );
        Hashtable               v_member            = null;

        String                  v_isb2c             = "N";  // tz_grcode에서 찾아온다.
        String                  v_ischkfirst        = "N";  // tz_subjseq에서 찾아온다.

        String                  v_appdate           = (String)data.get("appdate"        );
        String                  v_proptxt           = (String)data.get("proptxt"        );
        String                  v_chkfirsttxt       = (String)data.get("chkfirsttxt"    );
        String                  v_chkfirstmail      = (String)data.get("chkfirstmail"   );
        String                  v_chkfirstuserid    = (String)data.get("chkfirstuserid" );
        String                  v_chkfirstdate      = (String)data.get("chkfirstdate"   );
        String                  v_billstat          = (String)data.get("billstat"       );
        String                  v_ordcode           = (String)data.get("ordcode"        );
        String                  v_cancelkind        = (String)data.get("cancelkind"     );
        String                  v_rejectkind        = (String)data.get("rejectkind"     );
        String                  v_rejectedreason    = (String)data.get("rejectedreason" );

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr             = new DBConnectionManager();
                connMgr.setAutoCommit(false);
                v_CreateConnManager = true;
            }
            v_member = getMeberInfo(connMgr, v_userid);
            
            list    = getCourseToSubj(connMgr, v_subj, v_year, v_subjseq);

            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata   = (SubjseqData)list.get(i);
                
                sbSQL.setLength(0);

                // insert TZ_PROPOSE table
                sbSQL.append(" UPDATE Tz_Propose SET                                                                                                                    \n")
                     .append("         subj              = " + ( v_subj             == null ? "subj"               : SQLString.Format(v_subj                )) + "      \n")
                     .append("     ,   subjseq           = " + ( v_subjseq          == null ? "subjseq"            : SQLString.Format(v_subjseq             )) + "      \n")
                     .append("     ,   year              = " + ( v_year             == null ? "year"               : SQLString.Format(v_year                )) + "      \n")
                     .append("     ,   comp              = " + SQLString.Format((String)v_member.get("comp"    )) + "                                                   \n")
                     //.append("     ,   jik               = " + SQLString.Format((String)v_member.get("jikup"   )) + "                                                   \n")
                     .append("     ,   appdate           = " + ( v_appdate          == null ? "appdate"            : SQLString.Format(v_appdate             )) + "      \n")
                     .append("     ,   isdinsert         = " + ( v_isdinsert        == null ? "isdinsert"          : SQLString.Format(v_isdinsert           )) + "      \n")
                     .append("     ,   isb2c             = " + ( v_isb2c            == null ? "isb2c"              : SQLString.Format(v_isb2c               )) + "      \n")
                     .append("     ,   ischkfirst        = " + ( v_ischkfirst       == null ? "ischkfirst"         : SQLString.Format(v_ischkfirst          )) + "      \n")
                     .append("     ,   chkfirst          = " + ( v_chkfirst         == null ? "chkfirst"           : SQLString.Format(v_chkfirst            )) + "      \n")
                     .append("     ,   isproposeapproval = " + ( v_isproposeapproval== null ? "isproposeapproval"  : SQLString.Format(v_isproposeapproval   )) + "      \n")
                     .append("     ,   chkfinal          = " + ( v_chkfinal         == null ? "chkfinal"           : SQLString.Format(v_chkfinal            )) + "      \n")
                     .append("     ,   proptxt           = " + ( v_proptxt          == null ? "proptxt"            : SQLString.Format(v_proptxt             )) + "      \n")
                     .append("     ,   billstat          = " + ( v_billstat         == null ? "billstat"           : SQLString.Format(v_billstat            )) + "      \n")
                     .append("     ,   ordcode           = " + ( v_ordcode          == null ? "ordcode"            : SQLString.Format(v_ordcode             )) + "      \n")
                     .append("     ,   cancelkind        = " + ( v_cancelkind       == null ? "cancelkind"         : SQLString.Format(v_cancelkind          )) + "      \n");
                
                if ( v_chkfinal.equals("N") ) { 
                    sbSQL.append("     ,   rejectkind        = " + ( v_rejectkind       == null ? "rejectkind"         : SQLString.Format(v_rejectkind      )) + "      \n")
                         .append("     ,   rejectedreason    = " + ( v_rejectedreason   == null ? "rejectedreason"     : SQLString.Format(v_rejectedreason  )) + "      \n");
                } else { 
                    sbSQL.append("     ,   rejectkind        = ''                                                                                                       \n")
                         .append("     ,   rejectedreason    = ''                                                                                                       \n");
                }
                
                sbSQL.append("     ,   luserid           = " + ( v_luserid          == null ? "luserid"            : SQLString.Format(v_luserid             ))    + "   \n")
                     .append("     ,   ldate             = " + SQLString.Format( FormatDate.getDate("yyyyMMddHHmmss")) + "                                              \n")
                     .append(" WHERE   subj              = " + SQLString.Format( v_subjseqdata.getSubj()             ) + "                                              \n")
                     .append(" AND     year              = " + SQLString.Format( v_subjseqdata.getYear()             ) + "                                              \n")
                     .append(" AND     subjseq           = " + SQLString.Format( v_subjseqdata.getSubjseq()          ) + "                                              \n")
                     .append(" AND     userid            = " + SQLString.Format( v_userid                            ) + "                                              \n");
                     //.append(" AND     userid            = ?");
                //System.out.println(this.getClass().getName() + "." + "updatePropose() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
//                pstmt   = connMgr.prepareStatement(sbSQL.toString());
//                pstmt.setString(1, SQLString.Format(v_userid));
//                isOk    = pstmt.executeUpdate();
                isOk    = connMgr.executeUpdate(sbSQL.toString());
            }
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }        
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }


    public int updateChangeSeqPropose(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        String              sql     = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_appsubjseq = (String)data.get("appsubjseq");

        String v_userid = (String)data.get("userid");
        String v_isdinsert = (String)data.get("isdinsert");
        String v_chkfirst = (String)data.get("chkfirst");
        String v_chkfinal = (String)data.get("chkfinal");
        String v_isproposeapproval = (String)data.get("isproposeapproval");
        String v_luserid  = (String)data.get("luserid");
        Hashtable v_member = null;
        v_member = getMeberInfo(v_userid);

        String v_isb2c      = "N";  // tz_grcode에서 찾아온다.
        String v_ischkfirst = "N";  // tz_subjseq에서 찾아온다.

        String v_appdate = (String)data.get("appdate");
        String v_proptxt = (String)data.get("proptxt");
        String v_chkfirsttxt = (String)data.get("chkfirsttxt");
        String v_chkfirstmail = (String)data.get("chkfirstmail");
        String v_chkfirstuserid = (String)data.get("chkfirstuserid");
        String v_chkfirstdate = (String)data.get("chkfirstdate");
        String v_billstat = (String)data.get("billstat");
        String v_ordcode = (String)data.get("ordcode");
        String v_cancelkind = (String)data.get("cancelkind");

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            //System.out.println("connMgr=" +connMgr);

            list = getCourseToSubj(v_subj, v_year, v_subjseq);
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);

                // insert TZ_PROPOSE table
                sql =  "update TZ_PROPOSE  \n" ;
                sql +=  "    set            \n";
                sql +=  "       subj              = " + (v_subj         == null ? "subj"              : SQLString.Format(v_subj)) + ", \n";
                sql +=  "       subjseq           = " + (v_appsubjseq == null ? "subjseq"           : SQLString.Format(v_appsubjseq))    + ", \n";
                sql +=  "       year              = " + (v_year       == null ? "year"           : SQLString.Format(v_year))    + ", \n";
                sql +=  "       comp              = " + SQLString.Format((String)v_member.get("comp")) + ", \n";
                sql +=  "       jik               = " + SQLString.Format((String)v_member.get("jikup")) + ", \n";
                sql +=  "       appdate           = " + (v_appdate          == null ? "appdate"           : SQLString.Format(v_appdate))    + ", \n";
                sql +=  "       isdinsert         = " + (v_isdinsert        == null ? "isdinsert"         : SQLString.Format(v_isdinsert))  + ", \n";
                sql +=  "       isb2c             = " + (v_isb2c            == null ? "isb2c"             : SQLString.Format(v_isb2c))      + ", \n";
                sql +=  "       ischkfirst        = " + (v_ischkfirst       == null ? "ischkfirst"        : SQLString.Format(v_ischkfirst)) + ", \n";
                sql +=  "       chkfirst          = " + (v_chkfirst         == null ? "chkfirst"          : SQLString.Format(v_chkfirst))   + ", \n";
                sql +=  "       isproposeapproval = " + (v_isproposeapproval == null ? "isproposeapproval" : SQLString.Format(v_isproposeapproval))   + ", \n";
                sql +=  "       chkfinal          = " + (v_chkfinal         == null ? "chkfinal"          : SQLString.Format(v_chkfinal))   + ", \n";
                sql +=  "       proptxt           = " + (v_proptxt          == null ? "proptxt"           : SQLString.Format(v_proptxt))    + ", \n";
                sql +=  "       billstat          = " + (v_billstat         == null ? "billstat"          : SQLString.Format(v_billstat))   + ", \n";
                sql +=  "       ordcode           = " + (v_ordcode          == null ? "ordcode"           : SQLString.Format(v_ordcode))    + ", \n";
                sql +=  "       cancelkind        = " + (v_cancelkind       == null ? "cancelkind"        : SQLString.Format(v_cancelkind)) + ", \n";
                sql +=  "       luserid           = " + (v_luserid          == null ? "luserid"           : SQLString.Format(v_luserid))    + ", \n";
                sql +=  "       ldate             = " + SQLString.Format(FormatDate.getDate("yyyyMMddHHmmss")) + "\n";
                sql +=  " where subj              = " + SQLString.Format(v_subjseqdata.getSubj() ) + "\n";
                sql +=  "   and year              = " + SQLString.Format(v_subjseqdata.getYear() ) + "\n";
                sql +=  "   and subjseq           = " + SQLString.Format(v_subjseqdata.getSubjseq() ) + "\n";
                sql +=  "   and userid            = " + SQLString.Format(v_userid) + "\n";
                // System.out.println("sql_update=" +sql);

                isOk = connMgr.executeUpdate(sql);
                // isOk = 1;
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }


    public int updateStudent(Hashtable data) throws Exception { 
        DBConnectionManager     connMgr             = null;
        ArrayList               list                = null;
        SubjseqData             v_subjseqdata       = null;
        boolean                 v_CreateConnManager = false;
        StringBuffer            sbSQL               = new StringBuffer("");
        
        int                     iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        int                     isOk                = 0;

        String                  v_subj              = (String)data.get("subj"       );
        String                  v_year              = (String)data.get("year"       );
        String                  v_subjseq           = (String)data.get("subjseq"    );
        String                  v_appsubjseq        = (String)data.get("appsubjseq" );
        String                  v_userid            = (String)data.get("userid"     );
        String                  v_isdinsert         = (String)data.get("isdinsert"  );
        String                  v_chkfirst          = (String)data.get("chkfirst"   );
        String                  v_chkfinal          = (String)data.get("chkfinal"   );
        String                  v_luserid           = (String)data.get("luserid"    );
        Hashtable               v_member            = null;
        v_member                                    = getMeberInfo(v_userid);

        String                  v_isb2c             = "N";                              // tz_grcode에서 찾아온다.
        String                  v_edustart          = (String)data.get("edustart"   );  // 학습기간 설정
        String                  v_eduend            = (String)data.get("eduend"     );

        String                  v_class             = (String)data.get("class"      );
        String                  v_comp              = (String)data.get("comp"       );
        String                  v_score             = (String)data.get("score"      );
        String                  v_tstep             = (String)data.get("tstep"      );
        String                  v_mtest             = (String)data.get("mtest"      );
        String                  v_ftest             = (String)data.get("ftest"      );
        String                  v_report            = (String)data.get("report"     );
        String                  v_act               = (String)data.get("act"        );
        String                  v_etc1              = (String)data.get("etc1"       );
        String                  v_etc2              = (String)data.get("etc2"       );
        String                  v_avtstep           = (String)data.get("avtstep"    );
        String                  v_avmtest           = (String)data.get("avmtest"    );
        String                  v_avftest           = (String)data.get("avftest"    );
        String                  v_avreport          = (String)data.get("avreport"   );
        String                  v_avact             = (String)data.get("avact"      );
        String                  v_avetc1            = (String)data.get("avetc1"     );
        String                  v_avetc2            = (String)data.get("avetc2"     );
        String                  v_isgraduated       = (String)data.get("isgraduated");
        String                  v_isrestudy         = (String)data.get("isrestudy"  );
        String                  v_branch            = (String)data.get("branch"     );
        String                  v_confirmdate       = (String)data.get("confirmdate");
        String                  v_eduno             = (String)data.get("eduno"      );
        String                  v_stustatus         = (String)data.get("stustatus"  );

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                connMgr             = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            
            
            list    = getCourseToSubj(v_subj, v_year, v_subjseq);
            
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata   = (SubjseqData)list.get(i);

                sbSQL.append(" UPDATE  Tz_Student SET                                                                                          \n")    
                     .append("         subjseq     = " + ( v_appsubjseq    == null ? "subjseq"     : SQLString.Format( v_appsubjseq    )) + "  \n")
                     .append("     ,   class       = " + ( v_class         == null ? "class"       : SQLString.Format( v_class         )) + "  \n")
                     .append("     ,   comp        = " + ( v_comp          == null ? "comp"        : SQLString.Format( v_comp          )) + "  \n")
                     .append("     ,   isdinsert   = " + ( v_isdinsert     == null ? "isdinsert"   : SQLString.Format( v_isdinsert     )) + "  \n")
                     .append("     ,   score       = " + ( v_score         == null ? "score"       : SQLString.Format( v_score         )) + "  \n")
                     .append("     ,   tstep       = " + ( v_tstep         == null ? "tstep"       : SQLString.Format( v_tstep         )) + "  \n")
                     .append("     ,   mtest       = " + ( v_mtest         == null ? "mtest"       : SQLString.Format( v_mtest         )) + "  \n")
                     .append("     ,   ftest       = " + ( v_ftest         == null ? "ftest"       : SQLString.Format( v_ftest         )) + "  \n")
                     .append("     ,   report      = " + ( v_report        == null ? "report"      : SQLString.Format( v_report        )) + "  \n")
                     .append("     ,   act         = " + ( v_act           == null ? "act"         : SQLString.Format( v_act           )) + "  \n")
                     .append("     ,   etc1        = " + ( v_etc1          == null ? "etc1"        : SQLString.Format( v_etc1          )) + "  \n")
                     .append("     ,   etc2        = " + ( v_etc2          == null ? "etc2"        : SQLString.Format( v_etc2          )) + "  \n")
                     .append("     ,   avtstep     = " + ( v_avtstep       == null ? "avtstep"     : SQLString.Format( v_avtstep       )) + "  \n")
                     .append("     ,   avmtest     = " + ( v_avmtest       == null ? "avmtest"     : SQLString.Format( v_avmtest       )) + "  \n")
                     .append("     ,   avftest     = " + ( v_avftest       == null ? "avftest"     : SQLString.Format( v_avftest       )) + "  \n")
                     .append("     ,   avreport    = " + ( v_avreport      == null ? "avreport"    : SQLString.Format( v_avreport      )) + "  \n")
                     .append("     ,   avact       = " + ( v_avact         == null ? "avact"       : SQLString.Format( v_avact         )) + "  \n")
                     .append("     ,   avetc1      = " + ( v_avetc1        == null ? "avetc1"      : SQLString.Format( v_avetc1        )) + "  \n")
                     .append("     ,   avetc2      = " + ( v_avetc2        == null ? "avetc2"      : SQLString.Format( v_avetc2        )) + "  \n")
                     .append("     ,   isgraduated = " + ( v_isgraduated   == null ? "isgraduated" : SQLString.Format( v_isgraduated   )) + "  \n")
                     .append("     ,   isrestudy   = " + ( v_isrestudy     == null ? "isrestudy"   : SQLString.Format( v_isrestudy     )) + "  \n")
                     .append("     ,   isb2c       = " + ( v_isb2c         == null ? "isb2c"       : SQLString.Format( v_isb2c         )) + "  \n")
                     .append("     ,   edustart    = " + ( v_edustart      == null ? "edustart"    : SQLString.Format( v_edustart      )) + "  \n")
                     .append("     ,   eduend      = " + ( v_eduend        == null ? "eduend"      : SQLString.Format( v_eduend        )) + "  \n")
                     .append("     ,   branch      = " + ( v_branch        == null ? "branch"      : SQLString.Format( v_branch        )) + "  \n")
                     .append("     ,   confirmdate = " + ( v_confirmdate   == null ? "confirmdate" : SQLString.Format( v_confirmdate   )) + "  \n")
                     .append("     ,   eduno       = " + ( v_eduno         == null ? "eduno"       : SQLString.Format( v_eduno         )) + "  \n")
                     .append("     ,   stustatus   = " + ( v_stustatus     == null ? "stustatus"   : SQLString.Format( v_stustatus     )) + "  \n")
                     .append("     ,   luserid     = " + SQLString.Format(v_luserid                    ) + "                                   \n")
                     .append("     ,   ldate       = " + FormatDate.getDate("yyyyMMddHHmmss")            + "                                   \n")
                     .append(" WHERE   subj        = " + SQLString.Format(v_subjseqdata.getSubj()      ) + "                                   \n")
                     .append(" AND     year        = " + SQLString.Format(v_subjseqdata.getYear()      ) + "                                   \n")
                     .append(" AND     subjseq     = " + SQLString.Format(v_subjseqdata.getSubjseq()   ) + "                                   \n")
                     .append(" AND     userid      = " + SQLString.Format(v_userid                     ) + "                                   \n");
                     
                //System.out.println(this.getClass().getName() + "." + "updateStudent() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                     
                isOk    = connMgr.executeUpdate(sbSQL.toString());
            }
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }        
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }


    public int updateStudentreject(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        String              sql     = "";

        String v_subj       = (String)data.get("subj");
        String v_year       = (String)data.get("year");
        String v_subjseq    = (String)data.get("subjseq");
        String v_appsubjseq = (String)data.get("appsubjseq");
        String v_userid     = (String)data.get("userid");
        String v_isdinsert  = (String)data.get("isdinsert");
        String v_chkfirst   = (String)data.get("chkfirst");
        String v_chkfinal   = (String)data.get("chkfinal");
        String v_luserid    = (String)data.get("luserid");
        Hashtable v_member  = null;
        v_member = getMeberInfo(v_userid);

        String v_isb2c    = "N";  // tz_grcode에서 찾아온다.
        // 학습기간 설정
        String v_edustart = (String)data.get("edustart");
        String v_eduend   = (String)data.get("eduend");

        String v_class       = (String)data.get("class");
        String v_comp        = (String)data.get("comp"        );
        String v_score       = (String)data.get("score"       );
        String v_tstep       = (String)data.get("tstep"       );
        String v_mtest       = (String)data.get("mtest"       );
        String v_ftest       = (String)data.get("ftest"       );
        String v_report      = (String)data.get("report"      );
        String v_act         = (String)data.get("act"         );
        String v_etc1        = (String)data.get("etc1"        );
        String v_etc2        = (String)data.get("etc2"        );
        String v_avtstep     = (String)data.get("avtstep"     );
        String v_avmtest     = (String)data.get("avmtest"     );
        String v_avftest     = (String)data.get("avftest"     );
        String v_avreport    = (String)data.get("avreport"    );
        String v_avact       = (String)data.get("avact"       );
        String v_avetc1      = (String)data.get("avetc1"      );
        String v_avetc2      = (String)data.get("avetc2"      );
        String v_isgraduated = (String)data.get("isgraduated" );
        String v_isrestudy   = (String)data.get("isrestudy"   );
        String v_branch      = (String)data.get("branch"      );
        String v_confirmdate = (String)data.get("confirmdate" );
        String v_eduno       = (String)data.get("eduno"       );
        String v_stustatus   = (String)data.get("stustatus"   );

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            list = getCourseToSubj(v_subj, v_year, v_subjseq);
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);

                sql =  "update TZ_STUDENTREJECT " ;
                sql +=  "   set ";
                sql +=  "       subjseq     =" + (v_appsubjseq == null ? "subjseq"     : SQLString.Format(v_appsubjseq))  + ", ";
                sql +=  "       class       =" + (v_class     == null ? "class"       : SQLString.Format(v_class      )) + ", ";
                sql +=  "       comp        =" + (v_comp      == null ? "comp"        : SQLString.Format(v_comp       )) + ", ";
                sql +=  "       isdinsert   =" + (v_isdinsert == null ? "isdinsert"   : SQLString.Format(v_isdinsert  )) + ", ";
                sql +=  "       score       =" + (v_score     == null ? "score"       : SQLString.Format(v_score      )) + ", ";
                sql +=  "       tstep       =" + (v_tstep     == null ? "tstep"       : SQLString.Format(v_tstep      )) + ", ";
                sql +=  "       mtest       =" + (v_mtest     == null ? "mtest"       : SQLString.Format(v_mtest      )) + ", ";
                sql +=  "       ftest       =" + (v_ftest     == null ? "ftest"       : SQLString.Format(v_ftest      )) + ", ";
                sql +=  "       report      =" + (v_report    == null ? "report"      : SQLString.Format(v_report     )) + ", ";
                sql +=  "       act         =" + (v_act       == null ? "act"         : SQLString.Format(v_act        )) + ", ";
                sql +=  "       etc1        =" + (v_etc1      == null ? "etc1"        : SQLString.Format(v_etc1       )) + ", ";
                sql +=  "       etc2        =" + (v_etc2      == null ? "etc2"        : SQLString.Format(v_etc2       )) + ", ";
                sql +=  "       avtstep     =" + (v_avtstep   == null ? "avtstep"     : SQLString.Format(v_avtstep    )) + ", ";
                sql +=  "       avmtest     =" + (v_avmtest   == null ? "avmtest"     : SQLString.Format(v_avmtest    )) + ", ";
                sql +=  "       avftest     =" + (v_avftest   == null ? "avftest"     : SQLString.Format(v_avftest    )) + ", ";
                sql +=  "       avreport    =" + (v_avreport  == null ? "avreport"    : SQLString.Format(v_avreport   )) + ", ";
                sql +=  "       avact       =" + (v_avact     == null ? "avact"       : SQLString.Format(v_avact      )) + ", ";
                sql +=  "       avetc1      =" + (v_avetc1    == null ? "avetc1"      : SQLString.Format(v_avetc1     )) + ", ";
                sql +=  "       avetc2      =" + (v_avetc2    == null ? "avetc2"      : SQLString.Format(v_avetc2     )) + ", ";
                sql +=  "       isgraduated =" + (v_isgraduated == null ? "isgraduated" : SQLString.Format(v_isgraduated)) + ", ";
                sql +=  "       isrestudy   =" + (v_isrestudy == null ? "isrestudy"   : SQLString.Format(v_isrestudy  )) + ", ";
                sql +=  "       isb2c       =" + (v_isb2c     == null ? "isb2c"       : SQLString.Format(v_isb2c      )) + ", ";
                sql +=  "       edustart    =" + (v_edustart  == null ? "edustart"    : SQLString.Format(v_edustart   )) + ", ";
                sql +=  "       eduend      =" + (v_eduend    == null ? "eduend"      : SQLString.Format(v_eduend     )) + ", ";
                sql +=  "       branch      =" + (v_branch    == null ? "branch"      : SQLString.Format(v_branch     )) + ", ";
                sql +=  "       confirmdate =" + (v_confirmdate == null ? "confirmdate" : SQLString.Format(v_confirmdate)) + ", ";
                sql +=  "       eduno       =" + (v_eduno     == null ? "eduno"       : SQLString.Format(v_eduno      )) + ", ";
                sql +=  "       stustatus   =" + (v_stustatus == null ? "stustatus"   : SQLString.Format(v_stustatus  )) + ", ";
                sql +=  "       luserid     =" + SQLString.Format(v_luserid) + ", ";
                sql +=  "       ldate       =" + FormatDate.getDate("yyyyMMddHHmmss");
                sql +=  " where subj        =" + SQLString.Format(v_subjseqdata.getSubj() );
                sql +=  "   and year        =" + SQLString.Format(v_subjseqdata.getYear() );
                sql +=  "   and subjseq     =" + SQLString.Format(v_subjseqdata.getSubjseq() );
                sql +=  "   and userid      =" + SQLString.Format(v_userid);

                isOk = connMgr.executeUpdate(sql);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }

    public Hashtable FileToDB(Hashtable inputdata) { 
        Hashtable outputdata = new Hashtable();
        Hashtable insertdata = new Hashtable();
        Hashtable insertdata2 = new Hashtable();

        outputdata.put("p_errorcode", "1");

        DBConnectionManager connMgr = (DBConnectionManager)inputdata.get("connMgr");
        PreparedStatement propose_pstmt = (PreparedStatement)inputdata.get("propose_pstmt");
        PreparedStatement student_pstmt = (PreparedStatement)inputdata.get("student_pstmt");

        String  v_grcode     = (String)inputdata.get("p_grcode");
        String  v_gyear      = (String)inputdata.get("p_gyear");
        String  v_grseq      = (String)inputdata.get("p_grseq");
        String  v_subjcourse = (String)inputdata.get("p_subjcourse");
        String  v_luserid    = (String)inputdata.get("p_luserid");
        String  v_userid     = (String)inputdata.get("p_userid");
        String  v_name       = (String)inputdata.get("p_name");
        String  v_subjseq    = (String)inputdata.get("p_subjseq");
        String  v_inputlevel = (String)inputdata.get("p_inputlevel");
        String  v_isdinsert  = "Y";

        // 등록권한별 1차승인, 2차승인 결정
        String v_chkfirst   = "Y";
        String v_chkfinal   = "Y";
        int isOk = 0;

        // 수강신청일경우 미처리로 세팅
        if ( v_inputlevel.equals("propose") ) { 
            v_chkfirst   = "B";
            v_chkfinal   = "B";
        }

        try { 
            String  v_year = getSubjYear(v_grcode, v_gyear, v_grseq, v_subjcourse, v_subjseq);
            if ( v_year.equals("")) v_year = v_gyear;

            insertdata.put("connMgr",  connMgr);
            insertdata.put("subj",     v_subjcourse);
            insertdata.put("year",     v_year);
            insertdata.put("subjseq",  v_subjseq);
            insertdata.put("userid",   v_userid);
            insertdata.put("isdinsert",v_isdinsert);
            insertdata.put("chkfirst", v_chkfirst);
            insertdata.put("chkfinal", v_chkfinal);
            insertdata.put("luserid",  v_luserid);

            if ( propose_pstmt != null ) { 
                insertdata.put("propose_pstmt",  propose_pstmt);
            }

            isOk = insertPropose(insertdata);

            // if ( v_inputlevel.equals("student") ) { 
                if ( student_pstmt != null ) { 
                    insertdata.put("student_pstmt",  student_pstmt);
                }
                if ( isOk > 0 ) { 
                    insertStudent(insertdata);
                }
            // }
        } catch ( Exception ex ) { 
            outputdata.put("p_errorcode", "0");
            outputdata.put("p_exception", ex);
        } finally { 
        }

        return outputdata;
    }


    public Hashtable EduTargetFileToDB(Hashtable inputdata) { 
        Hashtable outputdata = new Hashtable();
        Hashtable insertdata = new Hashtable();
        // Hashtable insertdata2 = new Hashtable();

        outputdata.put("p_errorcode", "1");

        DBConnectionManager	connMgr	= null;
        PreparedStatement edutarget_pstmt = (PreparedStatement)inputdata.get("edutarget_pstmt");
        boolean v_CreateConnManager = false;


        String  v_grcode     = (String)inputdata.get("p_grcode");
        String  v_gyear      = (String)inputdata.get("p_gyear");
        String  v_grseq      = (String)inputdata.get("p_grseq");
        String  v_luserid    = (String)inputdata.get("p_luserid");
        String  v_userid     = (String)inputdata.get("p_userid");
        String  v_name       = (String)inputdata.get("p_name");
        String  v_mastercd   = (String)inputdata.get("p_mastercd");

        int isOk = 0;

        try { 
            connMgr = (DBConnectionManager)inputdata.get("connMgr");

            if ( connMgr == null ) { 
                    connMgr = new DBConnectionManager();
                    v_CreateConnManager = true;
            }

            insertdata.put("connMgr",  connMgr);
            insertdata.put("mastercd",     v_mastercd);
            insertdata.put("userid",   v_userid);
            insertdata.put("luserid",  v_luserid);

            if ( edutarget_pstmt != null ) { 
                insertdata.put("edutarget_pstmt",  edutarget_pstmt);
            }

            isOk = insertEduTarget(insertdata);

            //System.out.println("isOk=" +isOk);

        } catch ( Exception ex ) { 
            outputdata.put("p_errorcode", "0");
            outputdata.put("p_exception", ex);
        } finally { 
            if ( edutarget_pstmt != null )  { try { edutarget_pstmt.close(); } catch ( Exception e1 ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }

        }

        return outputdata;
    }



     /**
    기수변경신청시 업데이트
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int updateChangeSeq(Hashtable data) throws Exception { 
        DBConnectionManager	connMgr	= null;
        boolean v_CreateConnManager = false;
        DataBox             dbox    = null;
        PreparedStatement   pstmt   = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        String sql          = "";
        String sql1         = "";
        int isOk            = 0;

        //// //// //// //// //// P.Key//// //// //// //// //// //// ///
        String v_subj       = (String)data.get("subj");
        String v_subjseq    = (String)data.get("subjseq");
        String v_year       = (String)data.get("year");
        String v_userid     = (String)data.get("userid");
        //// //// //// //// //// P.Key//// //// //// //// //// //// ///

        String v_userjik    = (String)data.get("userjik");
        String v_usercomp   = (String)data.get("usercomp");
        String v_appsubj    = (String)data.get("appsubj");
        String v_appsubjseq = (String)data.get("appsubjseq");
        String v_appyear    = (String)data.get("appyear");
        String v_appid      = (String)data.get("appid");
        String v_appcono    = (String)data.get("appcono");
        String v_appmail    = (String)data.get("appmail");

        String v_content       = (String)data.get("content");
        String v_wantseq1      = (String)data.get("wantseq1");
        String v_wantseq2      = (String)data.get("wantseq2");
        String v_wantseq3      = (String)data.get("wantseq3");
        String v_isupapproval  = (String)data.get("isupapproval");
        String v_isdoapproval  = (String)data.get("isdoapproval");
        String v_isadmapproval = (String)data.get("isadmapproval");

        try { 
                connMgr = (DBConnectionManager)data.get("connMgr");
                if ( connMgr == null ) { 
                    connMgr = new DBConnectionManager();
                    v_CreateConnManager = true;
                }


                  sql1 =  "update TZ_CHANGESEQ " ;
                  sql1 +=  "  set            ";
                  sql1 +=  "    subj              = " + (v_subj      == null ? "appcono"         : SQLString.Format(v_subj))              + ", \n";
                  sql1 +=  "    subjseq           = " + (v_subjseq        == null ? "appid"           : SQLString.Format(v_subjseq))      + ", \n";
                  sql1 +=  "    year              = " + (v_year      == null ? "appmail"         : SQLString.Format(v_year))              + ", \n";
                  sql1 +=  "    userid            = " + (v_userid       == null ? "userid"          : SQLString.Format(v_userid))         + ", \n";
                  sql1 +=  "    usercomp          = " + (v_usercomp     == null ? "usercomp"        : SQLString.Format(v_usercomp))       + ", \n";
                  sql1 +=  "    userjik           = " + (v_userjik      == null ? "userjik"         : SQLString.Format(v_userjik))        + ", \n";
                  sql1 +=  "    content           = " + (v_content      == null ? "content"         : SQLString.Format(v_content))        + ", \n";
                  sql1 +=  "    wantseq1          = " + (v_wantseq1     == null ? "wantseq1"        : SQLString.Format(v_wantseq1))       + ", \n";
                  sql1 +=  "    wantseq2          = " + (v_wantseq2     == null ? "wantseq2"        : SQLString.Format(v_wantseq2))       + ", \n";
                  sql1 +=  "    wantseq3          = " + (v_wantseq3     == null ? "wantseq3"        : SQLString.Format(v_wantseq3))       + ", \n";

                  if ( v_isupapproval.equals("N")&&v_isdoapproval.equals("N") ) { 
                    sql1 +=  "    appcono           = '', \n";
                    sql1 +=  "    appid             = '', \n";
                    sql1 +=  "    appmail           = '', \n";
                    sql1 +=  "    appsubj           = '', \n";
                    sql1 +=  "    appsubjseq        = '', \n";
                    sql1 +=  "    appyear           = '', \n";
                    sql1 +=  "    isupapproval      = '', \n";
                    sql1 +=  "    isdoapproval      = '', \n";
                  } else { 
                    sql1 +=  "    appcono           = " + (v_appcono      == null ? "appcono"         : SQLString.Format(v_appcono))        + ", \n";
                    sql1 +=  "    appid             = " + (v_appid        == null ? "appid"           : SQLString.Format(v_appid))          + ", \n";
                    sql1 +=  "    appmail           = " + (v_appmail      == null ? "appmail"         : SQLString.Format(v_appmail))        + ", \n";
                    sql1 +=  "    appsubj           = " + (v_appsubj      == null ? "appsubj"         : SQLString.Format(v_appsubj))        + ", \n";
                    sql1 +=  "    appsubjseq        = " + (v_appsubjseq   == null ? "appsubjseq"      : SQLString.Format(v_appsubjseq))     + ", \n";
                    sql1 +=  "    appyear           = " + (v_appyear      == null ? "appyear"         : SQLString.Format(v_appyear))        + ", \n";
                    sql1 +=  "    isupapproval      = " + (v_isupapproval == null ? "isupapproval"    : SQLString.Format(v_isupapproval))   + ", \n";
                    sql1 +=  "    isdoapproval      = " + (v_isdoapproval == null ? "isdoapproval"    : SQLString.Format(v_isdoapproval))   + ", \n";
                  }
                  sql1 +=  "    isadmapproval     = " + (v_isadmapproval == null ? "isadmapproval"   : SQLString.Format(v_isadmapproval))  + "  \n";
                  sql1 +=  " where                                              \n";
                  sql1 +=  "    subj    = " +SQLString.Format(v_subj) + " \n";
                  sql1 +=  "    and subjseq = " +SQLString.Format(v_subjseq) + " \n";
                  sql1 +=  "    and year    = " +SQLString.Format(v_year) + " \n";
                  sql1 +=  "    and userid  = " +SQLString.Format(v_userid) + " \n";

                  isOk = connMgr.executeUpdate(sql1);

        }

        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }


    /**
    진도정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteProgress(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj    = (String)data.get("subj");
        String v_year    = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_gubun   = (String)data.get("gubun");
        String v_userid  = (String)data.get("userid");

        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            sql = "delete from TZ_PROGRESS where subj = ? and year = ? and subjseq = ? ";
            sql += " and userid = '" +v_userid + "'";

            pstmt = connMgr.prepareStatement(sql);

            list = getCourseToSubj(v_subj, v_year, v_subjseq);
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);

                pstmt.setString(1, v_subjseqdata.getSubj() );
                pstmt.setString(2, v_subjseqdata.getYear() );
                pstmt.setString(3, v_subjseqdata.getSubjseq() );

                isOk = pstmt.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


    /**
    진도정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteChiefAngency(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_comp    = (String)data.get("comp");

        ArrayList           list    = null;

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            sql = "delete from TZ_ChiefAgency where comp = ? ";
            //System.out.println(sql);

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_comp);

            isOk = pstmt.executeUpdate();
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


    //// //// //// //// //// //// //// //// //// //// //// //// /수강생 존재개수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public int getPropCount(String p_subj, String p_year, String p_subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int v_propcount = 0;
        try { 
            connMgr = new DBConnectionManager();
            v_propcount = getPropCount(connMgr, p_subj, p_year, p_subjseq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_propcount;
    }

    public int getPropCount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        ListSet             ls      = null;
        int v_propcount = 0;

        String sql  = "";

        try { 
            sql  = "select count(userid) cnt from tz_propose ";
            sql += " where subj  = " + SQLString.Format(p_subj);
            sql += " and year    = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_propcount = ls.getInt("cnt");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_propcount;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///수강생 존재개수 리턴끝//// //// //// //// //// //// //// //// //// //// //// //// //// // 



    //// //// //// //// //// //// //// //// //// //// //// //// /propose 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public int getOverPropCount(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int v_propcount = 0;
        try { 
            connMgr = new DBConnectionManager();
            v_propcount = getOverPropCount(connMgr, p_subj, p_year, p_subjseq, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_propcount;
    }

    public int getOverPropCount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        int v_propcount = 0;

        String sql  = "";

        try { 
            sql  = "select count(userid) cnt from tz_propose ";
            sql += " where subj  = " + SQLString.Format(p_subj);
            sql += " and year    = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and userid  = " + SQLString.Format(p_userid);
            //System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_propcount = ls.getInt("cnt");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_propcount;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///propose 리턴끝//// //// //// //// //// //// //// //// //// //// //// //// //// // 


    //// //// //// //// //// //// //// //// //// //// //// //// /student 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public int getOverStuCount(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int v_propcount = 0;
        try { 
            connMgr = new DBConnectionManager();
            v_propcount = getOverStuCount(connMgr, p_subj, p_year, p_subjseq, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_propcount;
    }

    public int getOverStuCount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        int v_propcount = 0;

        String sql  = "";

        try { 
            sql  = "select count(userid) cnt from tz_student ";
            sql += " where subj  = " + SQLString.Format(p_subj);
            sql += " and year    = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and userid  = " + SQLString.Format(p_userid);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_propcount = ls.getInt("cnt");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_propcount;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///student 리턴끝//// //// //// //// //// //// //// //// //// //// //// //// //// // 


    //// //// //// //// //// //// //// //// //// //// //// //// /studentreject 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public int getStuRejCount(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int v_propcount = 0;
        try { 
            connMgr = new DBConnectionManager();
            v_propcount = getStuRejCount(connMgr, p_subj, p_year, p_subjseq, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_propcount;
    }

    public int getStuRejCount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        int v_propcount = 0;

        String sql  = "";

        try { 
            sql  = "select count(userid) cnt from tz_studentreject ";
            sql += " where subj  = " + SQLString.Format(p_subj);
            sql += " and year    = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and userid  = " + SQLString.Format(p_userid);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_propcount = ls.getInt("cnt");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_propcount;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///studentreject 리턴끝//// //// //// //// //// //// //// //// //// //// //// //// //// // 


    //// //// //// //// //// //// //// //// //// //// //// //// /cancel 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public int getCancelCount(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int v_propcount = 0;
        try { 
            connMgr = new DBConnectionManager();
            v_propcount = getCancelCount(connMgr, p_subj, p_year, p_subjseq, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_propcount;
    }

    public int getCancelCount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        int v_propcount = 0;

        String sql  = "";

        try { 
            sql  = "select count(userid) cnt from tz_cancel ";
            sql += " where subj  = " + SQLString.Format(p_subj);
            sql += " and year    = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and userid  = " + SQLString.Format(p_userid);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_propcount = ls.getInt("cnt");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_propcount;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///cancel 리턴끝//// //// //// //// //// //// //// //// //// //// //// //// //// // 



    //// //// //// //// //// //// //// //// //// //// //// //// /stold 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public int getOverStoldCount(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int v_propcount = 0;
        try { 
            connMgr = new DBConnectionManager();
            v_propcount = getOverStoldCount(connMgr, p_subj, p_year, p_subjseq, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_propcount;
    }

    public int getOverStoldCount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        int v_propcount = 0;

        String sql  = "";

        try { 
            sql  = "select count(userid) cnt from tz_stold ";
            sql += " where subj  = " + SQLString.Format(p_subj);
            sql += " and year    = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and userid  = " + SQLString.Format(p_userid);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_propcount = ls.getInt("cnt");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_propcount;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///stold 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 




    //// //// //// //// //// //// //// //// //// //// //// //// /progress 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public int getOverProgressCount(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int v_propcount = 0;
        try { 
            connMgr = new DBConnectionManager();
            v_propcount = getOverProgressCount(connMgr, p_subj, p_year, p_subjseq, p_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_propcount;
    }

    public int getOverProgressCount(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet             ls      = null;
        int v_propcount = 0;

        String sql  = "";

        try { 
            sql  = "select count(userid) cnt from tz_progress ";
            sql += " where subj  = " + SQLString.Format(p_subj);
            sql += " and year    = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and userid  = " + SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_propcount = ls.getInt("cnt");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_propcount;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///progress 존재갯수//// //// //// //// //// //// //// //// //// //// //// //// //// // 

    /**
    수강 책 삭제 - 독서교육
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteProposeBook(Hashtable data) throws Exception { 
        boolean             v_CreateConnManager = false;
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ArrayList           list                = null;
        SubjseqData         v_subjseqdata       = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 1;
        
        String              v_subj              = (String)data.get("subj"   );
        String              v_year              = (String)data.get("year"   );
        String              v_subjseq           = (String)data.get("subjseq");
        String              v_userid            = (String)data.get("userid" );

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                v_CreateConnManager = true;

                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);
            }

            sbSQL.append(" delete from TZ_PROPOSEBOOK   \n")
                 .append(" where   subj    = ?      \n")
                 .append(" and     year    = ?      \n")
                 .append(" and     subjseq = ?      \n")
                 .append(" and     userid  = ?      \n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            list    = getCourseToSubj(v_subj, v_year, v_subjseq);
            
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata   = (SubjseqData)list.get(i);

                pstmt.setString(1, v_subjseqdata.getSubj()      );
                pstmt.setString(2, v_subjseqdata.getYear()      );
                pstmt.setString(3, v_subjseqdata.getSubjseq()   );
                pstmt.setString(4, v_userid                     );

                pstmt.executeUpdate();
            }
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }        
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { } 
            }
        
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }
    
    /**
    수강 배송지 정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int deleteDeliveryInfo(Hashtable data) throws Exception { 
        boolean             v_CreateConnManager = false;
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ArrayList           list                = null;
        SubjseqData         v_subjseqdata       = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 1;
        
        String              v_subj              = (String)data.get("subj"   );
        String              v_year              = (String)data.get("year"   );
        String              v_subjseq           = (String)data.get("subjseq");
        String              v_userid            = (String)data.get("userid" );

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                v_CreateConnManager = true;

                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);
            }

            sbSQL.append(" delete from tz_delivery   \n")
                 .append(" where   subj    = ?      \n")
                 .append(" and     year    = ?      \n")
                 .append(" and     subjseq = ?      \n")
                 .append(" and     userid  = ?      \n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            pstmt.setString(1, v_subj  );
            pstmt.setString(2, v_year );
            pstmt.setString(3, v_subjseq );
            pstmt.setString(4, v_userid                     );

            pstmt.executeUpdate();
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }        
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { } 
            }
        
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }
    
    /**
    상태값 수정 - 독서교육
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int updateStatus(Hashtable data) throws Exception { 
        boolean             v_CreateConnManager = false;
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ArrayList           list                = null;
        SubjseqData         v_subjseqdata       = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 1;
        
        String              v_subj              = (String)data.get("subj"   );
        String              v_year              = (String)data.get("year"   );
        String              v_subjseq           = (String)data.get("subjseq");
        String              v_userid            = (String)data.get("userid" );
        String              v_status            = (String)data.get("status" );
        
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                v_CreateConnManager = true;

                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);
            }

            sbSQL.append(" update TZ_PROPOSEBOOK set status = ?  	\n")
                 .append(" where   subj    = ?      				\n")
                 .append(" and     year    = ?      				\n")
                 .append(" and     subjseq = ?      				\n")
                 .append(" and     userid  = ?      				\n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());

            list    = getCourseToSubj(v_subj, v_year, v_subjseq);
            
            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata   = (SubjseqData)list.get(i);
                
                pstmt.setString(1, v_status);
                pstmt.setString(2, v_subjseqdata.getSubj()      );
                pstmt.setString(3, v_subjseqdata.getYear()      );
                pstmt.setString(4, v_subjseqdata.getSubjseq()   );
                pstmt.setString(5, v_userid                     );

                pstmt.executeUpdate();
            }
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }        
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { } 
            }
        
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }
    
    /**
    독서교육 과정 도서 등록
    @param Hashtable   
    @return int        
    */
    public int insertProposeBook(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String sql1 = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");

        String v_userid = (String)data.get("userid");
        
        String v_bookcode = (String)data.get("bookcode");
        String v_month = (String)data.get("month");
        
        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        sql1 = "insert into tz_proposebook(subj, year, subjseq, userid, month, bookcode, status)	\n"
             + "       values(?, ?, ?, ?, ?, ?, ?)													\n";
        
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            if ( pstmt == null ) { 
            	pstmt = connMgr.prepareStatement(sql1);
                v_CreatePreparedStatement = true;
            }
           
            list = getCourseToSubj(connMgr, v_subj, v_year, v_subjseq);

            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);
           
                pstmt.setString( 1, v_subjseqdata.getSubj() );
                pstmt.setString( 2, v_subjseqdata.getYear() );
                pstmt.setString( 3, v_subjseqdata.getSubjseq() );
                pstmt.setString( 4, v_userid);
                pstmt.setInt( 5, Integer.parseInt(v_month));
                pstmt.setInt( 6, Integer.parseInt(v_bookcode));
                pstmt.setString( 7, "W");

                isOk = pstmt.executeUpdate();

            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( v_CreatePreparedStatement) { 
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            }
        }

        return isOk;
    }
    
    public String getEmailMeberInfo(DBConnectionManager connMgr, String p_userid) throws Exception {
        ListSet ls = null;
        String sql  = "";
        String result = "";

        try {
            sql = " select a.name,  case when a.email = '' or a.email is null then 'xxx' else a.email end email \n";
            sql+= "   from tz_member a                         ";
            sql+= " where a.userid = '"+p_userid+"'            ";
            

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                result = "1|" + ls.getString("name") + "|" + ls.getString("email");
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return result;
    }
    
    public String getSendMemberInfo(DBConnectionManager connMgr, String p_userid, String p_subj, String p_year, String p_subjseq) throws Exception {
        ListSet ls = null;
        String sql  = "";
        String result = "";

        try {
            sql  = "SELECT A.USERID  \n";
            sql += "     , A.NAME  \n";
            sql += "     , A.EMAIL  \n";
            sql += "     , A.HANDPHONE  \n";
            sql += "     , A.HANDPHONE_NO  \n";
            sql += "     , B.SUBJNM  \n";
            sql += "     , B.EDUSTART  \n";
            sql += "     , B.EDUEND  \n";
            sql += "     , B.TSTEP  \n";
            sql += "     , B.STUDY_TIME  \n";
            sql += "FROM   TZ_MEMBER A  \n";
            sql += "     ,(SELECT B.USERID  \n";
            sql += "            , A.SUBJNM  \n";
            sql += "            , TO_CHAR(TO_DATE(A.EDUSTART, 'YYYYMMDDHH24'), 'YYYY.MM.DD') AS EDUSTART  \n";
            sql += "            , TO_CHAR(TO_DATE(A.EDUEND, 'YYYYMMDDHH24'), 'YYYY.MM.DD') AS EDUEND  \n";
            sql += "            , B.TSTEP  \n";
            sql += "            , B.STUDY_TIME  \n";
            sql += "       FROM   TZ_SUBJSEQ A  \n";
            sql += "            , TZ_STUDENT B  \n";
            sql += "       WHERE  A.SUBJ = B.SUBJ (+)  \n";
            sql += "       AND    A.YEAR = B.YEAR (+)  \n";
            sql += "       AND    A.SUBJSEQ = B.SUBJSEQ (+)  \n";
            sql += "       AND    A.SUBJ = " + SQLString.Format(p_subj) + "  \n";
            sql += "       AND    A.YEAR = " + SQLString.Format(p_year) + "  \n";
            sql += "       AND    A.SUBJSEQ = " + SQLString.Format(p_subjseq) + ") B  \n";
            sql += "WHERE  A.USERID = B.USERID (+)  \n";
            sql += "AND    A.USERID = " + SQLString.Format(p_userid) + "  \n";
            sql += "AND    NVL(A.ISMAILLING,'Y') ='Y'  \n ";

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                result = ls.getString("userid") + "|" + ls.getString("name") + "|" + ls.getString("email");
                result += "|" + ls.getString("subjnm") + "|" + ls.getString("edustart") + "|" + ls.getString("eduend");
                result += "|" + ls.getString("tstep") + "|" + ls.getString("study_time");
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return result;
    }
    
    /**
    독서교육 과정 배송지 정보 등록
    @param Hashtable   
    @return int        
    */
    public int insertDelivery(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String sql1 = "";

        String v_subj = (String)data.get("subj");
        String v_year = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");

        String v_userid = (String)data.get("userid");
        
        String v_post1 = (String)data.get("post1");
        String v_post2 = (String)data.get("post2");
        String v_address1 = (String)data.get("address1");
        String v_address2 = (String)data.get("address2");
        String v_handphone = (String)data.get("handphone");
        
        ArrayList           list    = null;
        SubjseqData v_subjseqdata = null;

        sql1= "insert into tz_delivery( "
        	+ "       subj "
        	+ "     , year "
        	+ "     , subjseq "
        	+ "     , userid "
        	+ "     , delivery_post1 "
        	+ "     , delivery_post2 "
        	+ "     , delivery_address1 "
        	+ "     , delivery_address2 "
        	+ "     , delivery_handphone "
        	+ "     , luserid "
        	+ "     , ldate "
        	+ "      ) "
            + "values(? "
            + "     , ? "
            + "     , ? "
            + "     , ? "
            + "     , ? "
            + "     , ? "
            + "     , ? "
            + "     , ? "
            + "     , ? "
            + "     , ? "
            + "     , to_char(sysdate, 'yyyymmddhh24miss') "
            + "      ) ";
        
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            pstmt = connMgr.prepareStatement(sql1);
           
            list = getCourseToSubj(connMgr, v_subj, v_year, v_subjseq);

            for ( int i = 0; i<list.size(); i++ ) { 
                v_subjseqdata = (SubjseqData)list.get(i);
           
                pstmt.setString( 1, v_subjseqdata.getSubj() );
                pstmt.setString( 2, v_subjseqdata.getYear() );
                pstmt.setString( 3, v_subjseqdata.getSubjseq() );
                pstmt.setString( 4, v_userid);
                pstmt.setString( 5, v_post1);
                pstmt.setString( 6, v_post2);
                pstmt.setString( 7, v_address1);
                pstmt.setString( 8, v_address2);
                pstmt.setString( 9, v_handphone);
                pstmt.setString(10, v_userid);
                
                isOk = pstmt.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( v_CreatePreparedStatement) { 
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            }
        }

        return isOk;
    }

	/**
	 * 입과처리, 수강생 추가시 메일발송
	 * @param box
	 * @return
	 * @throws Exception
	 */  
    public int sendMail(DBConnectionManager connMgr, RequestBox box, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
        ListSet    ls      = null;
        String     sql     = "";
        ConfigSet  conf	   = new ConfigSet();
        String	   v_subjnm= GetCodenm.get_subjnm(p_subj);
        int		   isOk	   = 0;
        String	   v_email = "";
        String     v_content = "";
        
        
        try { 
            sql = "\n select userid, name, email "
            	+ "\n from   tz_member "
            	+ "\n where  userid = " + StringManager.makeSQL(p_userid);
            
            ls = connMgr.executeQuery(sql);

            box.put("p_mail_code", "003");
            box.put("from_name", conf.getProperty("mail.admin.name"));
            box.put("from_email", conf.getProperty("mail.admin.email"));
            box.put("p_title", "[e-Eureka] " + v_subjnm + " 과정에 입과 완료되었습니다.");
            box.put("p_content", "");
            box.put("p_map1", v_subjnm);
            box.put("p_map2", p_year);
            box.put("p_map3", p_subjseq);

            while(ls.next()) {
            	v_email = ls.getString("email");
            	if (!"".equals(StringManager.trim(v_email))) {
	 	            Vector v_to = new Vector();
	 	            v_to.addElement(ls.getString("userid") + "|" + ls.getString("name") + "|" + v_email);
	 	            box.put("to", v_to);
	 	            v_content = ls.getString("name") + "님 안녕하세요? e-Eureka 운영자입니다. <br><br> ";
	 	            v_content += ls.getString("name") + "님께서는 <span style='color:#ff4800;font-weight:bold;'>" + v_subjnm + "</span> 과정에 입과 완료되었습니다.<br>";
	 	            v_content += ls.getString("name") + "님 오늘 하루도 행복한 하루 되시길 바랍니다.";
	 	            box.put("p_content", v_content);
	 	            FreeMailBean bean = new FreeMailBean();
	 	            isOk = bean.amailSendMail(box);
            	}
            }
        
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
    /**
    학습정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int UpdatePayData(Hashtable data, String order_id) throws Exception { 

        boolean             v_CreateConnManager = false;
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ArrayList           list                = null;
        SubjseqData         v_subjseqdata       = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        StringBuffer        sbSQL1               = new StringBuffer("");
        int                 isOk                = 1;
        String v_subj       = (String)data.get("subj");
        String v_year       = (String)data.get("year");
        String v_subjseq    = (String)data.get("subjseq");
        String v_userid     = (String)data.get("userid");
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                v_CreateConnManager = true;
                connMgr = new DBConnectionManager();
            }

            sbSQL.append(" update pa_payment set useyn='N'  \n")
	             .append(" where   order_id    ='"+order_id+"'      \n");
	             
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            isOk = pstmt.executeUpdate();
            
            sbSQL1.append(" update tz_cancel set order_id= ?  \n")
                 .append(" where   subj = ?      \n")
            	 .append(" and     subjseq = ?      \n")
            	 .append(" and     userid = ?      \n")
            	 .append(" and     year = ?      \n")
            	 .append(" and     seq = (select max(seq) from tz_cancel       \n")
		         .append(" where   subj = ?      \n")
		       	 .append(" and     subjseq = ?      \n")
		       	 .append(" and     userid = ?      \n")
		       	 .append(" and     year = ? )     \n");
		   
	       pstmt   = connMgr.prepareStatement(sbSQL1.toString());
	       int index=1;
	       pstmt.setString(index++,order_id);
	       pstmt.setString(index++,v_subj);
	       pstmt.setString(index++,v_subjseq);
	       pstmt.setString(index++,v_userid);
	       pstmt.setString(index++,v_year);
	       pstmt.setString(index++,v_subj);
	       pstmt.setString(index++,v_subjseq);
	       pstmt.setString(index++,v_userid);
	       pstmt.setString(index++,v_year);
	       
	       isOk = pstmt.executeUpdate();

        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { } 
            }
        
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return isOk;
    }


}
