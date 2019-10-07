// **********************************************************
// 1. 제      목:
// 2. 프로그램명: MyPageSuRoyBean.java
// 3. 개      요: 마이페이지 - 수료증 관련 빈
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: 조용준 2006.07.12
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.mypage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Hashtable;

import com.ziaan.common.SubjComBean;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.propose.ProposeBean;

/**
 * @author 김민수
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class MyPageSuRoyBean { 
    public static String SINGLE_CLASS = "1";
    public static String PLURAL_CLASS = "2";

    public static String CLASSNM = "클래스";
    public static String SINGLE_CLASS_CODE = "0001";

    private     ConfigSet   config;
    private     int         row;
    
    public MyPageSuRoyBean() {
        try { 
            config          = new ConfigSet();
            row             = Integer.parseInt(config.getProperty("page.bulletin.row") ); // 이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    수료증 신청 리스트 보여주기
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectMySuRoyList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        DataBox dbox        = null;

        
        String  v_user_id   = box.getSession("userid");
        int v_pageno = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  =
                "\n	select tc.subj, tc.year, tc.subjseq, tc.userid, tc.seq, tc.ea, 		" +
                "\n		tc.appdate, tc.senddate, tc.issend,				" +
                "\n		ts.subjnm, ts.isonoff							" +
                "\n	from tz_certapp tc, tz_subj ts					" +
                "\n	where tc.userid		= ':userid'					" +
                "\n		and tc.subj	= ts.subj					" +                
                "\n	order by tc.appdate desc						";
                                

            sql1 = sql1.replaceAll( ":userid", v_user_id );

           ls1 = connMgr.executeQuery(sql1);
            ls1.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            ls1.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            int totalpagecount = ls1.getTotalPage();    //     전체 페이지 수를 반환한다
            int totalrowcount = ls1.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls1.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
                list1.add(dbox);
            }
            ls1.close();
            
                        
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
     나의 발급 완료된 수료증수
     @param box      receive from the form object and session
     @return ArrayList
     */
    public DataBox selectMySuRoyCount(RequestBox box) throws Exception 
    {
         
         DBConnectionManager    connMgr = null;
         ListSet ls1          = null;
         ArrayList list1      = null;
         String sql1          = "";
         DataBox dbox         = null;
         String  v_user_id    = box.getSession("userid");

         try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();
             
             sql1  = "";        
             sql1 += "  select count(*) CNT				";
             sql1 += "	from tz_certapp 				";
             sql1 += "	where userid=" + SQLString.Format(v_user_id)	 ;
             sql1 += "	 and issend='Y' ";
             
             System.out.println("sql1 ==  ==  ==  ==  ==  == > 완료" +sql1);
             ls1 = connMgr.executeQuery(sql1);

             for ( int i = 0; ls1.next(); i++ ) {                  
                 dbox = ls1.getDataBox();
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return dbox;
     }
     
     /**
     수료증 신청/발급 1단계 필요 회원정보
     @param box      receive from the form object and session
     @return ArrayList
     */
    public DataBox selectMySuRoyStep1(RequestBox box) throws Exception 
    {
         
         DBConnectionManager    connMgr = null;
         ListSet ls1          = null;
         ArrayList list1      = null;
         String sql1          = "";
         DataBox dbox         = null;
         String  v_user_id    = box.getSession("userid");

         try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();
             
             sql1  = "";        
             sql1 += "  select userid, name, fn_crypt('2', birth_date, 'knise') birth_date			";
             sql1 += "	from tz_member					";
             sql1 += "	where userid=" + SQLString.Format(v_user_id)	 ;
                          
             
             System.out.println("sql1 ==  ==  ==  ==  ==  == > 완료" +sql1);
             ls1 = connMgr.executeQuery(sql1);

             for ( int i = 0; ls1.next(); i++ ) {                  
                 dbox = ls1.getDataBox();
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return dbox;
     }     
     

    /**
    수료증 신청/발급 2단계 필요 이수한 과목 정보
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectMySuRoyStep2(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        DataBox dbox        = null;

        
        String  v_user_id   = box.getSession("userid");        

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  =
                "\n	select tsd.subj, tsd.year, tsd.subjseq, tsd.userid,		" +
                "\n		 tsj.subjnm,						" +
                "\n		 tsa.classname,						" +
                "\n		 tss.edustart, tss.eduend				" +
                "\n	from tz_stold tsd, tz_subj tsj, tz_subjatt tsa, tz_subjseq tss	" +
                "\n	where tsd.userid=" + SQLString.Format(v_user_id)		  +
                "\n		 and tsd.subj  = tsj.subj				" +
                "\n		 and tsj.subjclass  = tsa.subjclass			" +
                "\n		 and tsj.upperclass = tsa.upperclass			" +
                "\n		 and tsj.middleclass = tsa.middleclass			" +
                "\n		 and tsj.lowerclass = tsa.lowerclass			" +
                "\n		 and tsd.subj		= tss.subj			" +
                "\n		 and tsd.year		= tss.year			" +
                "\n		 and tsd.subjseq	= tss.subjseq			" +
                "\n		 and tsd.isgraduated='Y'				" +
                "\n	order by tsa.classname, tsj.subjnm, tss.edustart desc		" ;
                                

           ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();                
                list1.add(dbox);
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
    수료증신청 저장
    @param box      receive from the form object and session
    @return int
    */
     public int SuRoyAppInsert(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet ls1         = null;
        
        String sql1         = "";
        int cancel_cnt      = 0;
        int isOk            = 0;
        String  v_subj      = box.getString("p_subj");
        String  v_year      = box.getString("p_year");
        String  v_subjseq   = box.getString("p_subjseq");
        String  v_userid    = box.getString("p_userid");
        
        int 	v_seq	    = 1;
        
        String 	v_username  = box.getString("p_username");
        String 	v_useradd   = box.getString("p_useradd");
        String 	v_useremail = box.getString("p_useremail");
        String 	v_usertel   = box.getString("p_usertel");
        
        int 	v_ea	    = box.getInt("p_ea");

        Hashtable insertData = new Hashtable();
        ProposeBean probean = new ProposeBean();

        SubjComBean scbean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();

            sql1  = "	select nvl(max(seq),0)+1 maxseq ";
            sql1 += "	from tz_certapp ";
            sql1 += "	where subj = " +SQLString.Format(v_subj);
            sql1 += "   and year=" +SQLString.Format(v_year);            
            sql1 += "   and subjseq=" +SQLString.Format(v_subjseq);            
            sql1 += "   and userid=" +SQLString.Format(v_userid);
            
            ls1 = connMgr.executeQuery(sql1);
            ls1.next();

            v_seq = ls1.getInt("maxseq");
                            
            connMgr.setAutoCommit(false);

			sql1 =	"	insert into TZ_CERTAPP (subj, year, subjseq, userid, seq, ";
			sql1 +=	"		username, useradd, useremail, usertel, ea, ";
			sql1 +=	"		appdate, issend, senddate )";
			sql1 +=	"	values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ";
			sql1 +=	"		'N', '')";
			
			pstmt = connMgr.prepareStatement(sql1);
			pstmt.setString(1, v_subj);
			pstmt.setString(2, v_year);
			pstmt.setString(3, v_subjseq);
			pstmt.setString(4, v_userid);
			pstmt.setInt(5, v_seq);
			pstmt.setString(6, v_username);
			pstmt.setString(7, v_useradd);
			pstmt.setString(8, v_useremail);
			pstmt.setString(9, v_usertel);
			pstmt.setInt(10, v_ea);
			
			isOk = pstmt.executeUpdate();

            if ( isOk > 0 ) connMgr.commit();
            else connMgr.rollback();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
        	if ( ls1 != null )  { try { ls1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) {
            	try { 
            		connMgr.setAutoCommit(true); 
            		connMgr.freeConnection();
            	} catch ( Exception e ) { }
            }
        }
        return isOk;
    }     
    

/**
    온라인 수료증신청 저장
    @param box      receive from the form object and session
    @return int
    */
     public int SuRoyAppInsert032(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet ls1         = null;
        
        String sql1         = "";
        int cancel_cnt      = 0;
        int isOk            = 0;
        String  v_subj      = box.getString("p_subj");
        String  v_year      = box.getString("p_year");
        String  v_subjseq   = box.getString("p_subjseq");
        String  v_userid    = box.getString("p_userid");
        
        int 	v_seq	    = 1;
        
        String 	v_username  = box.getString("p_username");
        String 	v_useradd   = box.getString("p_useradd");
        String 	v_useremail = box.getString("p_useremail");
        String 	v_usertel   = box.getString("p_usertel");
        
        int 	v_ea	    = box.getInt("p_ea");

        Hashtable insertData = new Hashtable();
        ProposeBean probean = new ProposeBean();

        SubjComBean scbean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();

            sql1  = "	select nvl(max(seq),0)+1 maxseq ";
            sql1 += "	from tz_certapp ";
            sql1 += "	where subj = " +SQLString.Format(v_subj);
            sql1 += "   and year=" +SQLString.Format(v_year);            
            sql1 += "   and subjseq=" +SQLString.Format(v_subjseq);            
            sql1 += "   and userid=" +SQLString.Format(v_userid);
            
            ls1 = connMgr.executeQuery(sql1);
            ls1.next();

            v_seq = ls1.getInt("maxseq");
                            
            connMgr.setAutoCommit(false);

                      sql1 =	"	insert into TZ_CERTAPP (subj, year, subjseq, userid, seq, ";
                      sql1 +=	"		username, useradd, useremail, usertel, ea, ";
                      sql1 +=	"		appdate, issend, senddate, ison, sname, suserid )";
                      sql1 +=	"	values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ";
                      sql1 +=	"		'Y', to_char(sysdate,'YYYYMMDDHH24MISS'), 'Y', ?, ?)";
                      pstmt = connMgr.prepareStatement(sql1);
                      pstmt.setString(1, v_subj);
                      pstmt.setString(2, v_year);
                      pstmt.setString(3, v_subjseq);
                      pstmt.setString(4, v_userid);
                      pstmt.setInt(5, v_seq);
                      pstmt.setString(6, v_username);
                      pstmt.setString(7, v_useradd);
                      pstmt.setString(8, v_useremail);
                      pstmt.setString(9, v_usertel);
                      pstmt.setInt(10, v_ea);
                      pstmt.setString(11, v_username);
                      pstmt.setString(12, v_userid);
                      isOk = pstmt.executeUpdate();

                if ( isOk > 0 ) connMgr.commit();
                else connMgr.rollback();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }     
    

/**
    오프라인 수료증신청 저장
    @param box      receive from the form object and session
    @return int
    */
     public int SuRoyAppInsert232(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet ls1         = null;
        
        String sql1         = "";
        int cancel_cnt      = 0;
        int isOk            = 0;
        String  v_subj      = box.getString("p_subj");
        String  v_year      = box.getString("p_year");
        String  v_subjseq   = box.getString("p_subjseq");
        String  v_userid    = box.getString("p_userid");
        
        int 	v_seq	    = 1;
        
        String 	v_username  = box.getString("p_username");
        String 	v_useradd   = box.getString("p_useradd");
        String 	v_useremail = box.getString("p_useremail");
        String 	v_usertel   = box.getString("p_usertel");
        
        int 	v_ea	    = box.getInt("p_ea");

        Hashtable insertData = new Hashtable();
        ProposeBean probean = new ProposeBean();

        SubjComBean scbean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();

            sql1  = "	select nvl(max(seq),0)+1 maxseq ";
            sql1 += "	from tz_certapp ";
            sql1 += "	where subj = " +SQLString.Format(v_subj);
            sql1 += "   and year=" +SQLString.Format(v_year);            
            sql1 += "   and subjseq=" +SQLString.Format(v_subjseq);            
            sql1 += "   and userid=" +SQLString.Format(v_userid);
            
            ls1 = connMgr.executeQuery(sql1);
            ls1.next();

            v_seq = ls1.getInt("maxseq");
                            
            connMgr.setAutoCommit(false);

                      sql1 =	"	insert into TZ_CERTAPP (subj, year, subjseq, userid, seq, ";
                      sql1 +=	"		username, useradd, useremail, usertel, ea, ";
                      sql1 +=	"		appdate, issend, senddate, ison )";
                      sql1 +=	"	values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ";
                      sql1 +=	"		'N', '', 'N')";
                      pstmt = connMgr.prepareStatement(sql1);
                      pstmt.setString(1, v_subj);
                      pstmt.setString(2, v_year);
                      pstmt.setString(3, v_subjseq);
                      pstmt.setString(4, v_userid);
                      pstmt.setInt(5, v_seq);
                      pstmt.setString(6, v_username);
                      pstmt.setString(7, v_useradd);
                      pstmt.setString(8, v_useremail);
                      pstmt.setString(9, v_usertel);
                      pstmt.setInt(10, v_ea);
                      isOk = pstmt.executeUpdate();

                if ( isOk > 0 ) connMgr.commit();
                else connMgr.rollback();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }             
     
}












































