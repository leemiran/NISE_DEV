// **********************************************************
// 1. 제      목: 대상자 설문 대상자 관리
// 2. 프로그램명: SulmunTargetMemberBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.research;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SulmunTargetMemberBean { 

    public SulmunTargetMemberBean() { }


    /**
    설문 분류_직급 대상자 리스트
    @param box          receive from the form object and session
    @return ArrayList   설문 대상자
     */
    public ArrayList selectSulmunPostList(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr         = null;
    	ArrayList           list            = new ArrayList();
    	ListSet             ls              = null;
    	DataBox             dbox            = null;
    	StringBuffer        sbSQL           = new StringBuffer("");
    	
    	int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
    	
    	String              ss_grcode       = box.getString("s_grcode"      );
    	String              ss_year         = box.getString("s_gyear"       );
    	String              v_action        = box.getString("p_action"      );
    	int                 v_sulpapernum   = box.getInt   ("s_sulpapernum" );
    	
    	try { 
    			connMgr = new DBConnectionManager();
    			
    			sbSQL.append(" SELECT class_cd, class_nm FROM tk_hap009t WHERE close_dt = '        ' order by class_cd	\n");    	
    			
    			System.out.println(this.getClass().getName() + "." + "selectSulmunMemberList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
    			
    			ls      = connMgr.executeQuery(sbSQL.toString());
    			
    			while ( ls.next() ) { 
    				dbox = ls.getDataBox();
    				
    				list.add(dbox);
    			}
    	} catch ( SQLException e ) {
    		ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
    		throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	} catch ( Exception e ) {
    		ErrorManager.getErrorStackTrace(e, box, "");
    		throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	} finally {
    		if ( ls != null ) { 
    			try { 
    				ls.close();  
    			} catch ( Exception e ) { } 
    		}
    		
    		if ( connMgr != null ) { 
    			try { 
    				connMgr.freeConnection(); 
    			} catch ( Exception e ) { } 
    		}
    	}
    	
    	return list;
    }
    
    
    /**
    설문 분류군 대상자 리스트& 카운트
    performTargetMemberSelectPage
    @param box          receive from the form object and session
    @return ArrayList   설문 대상자
     */
    public ArrayList selectSulmunBunruMemberList(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr         = null;
    	ArrayList           list            = new ArrayList();
    	ListSet             ls              = null;
    	DataBox             dbox            = null;
    	StringBuffer        sbSQL           = new StringBuffer("");
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
    	
    	String              ss_grcode       = box.getString("s_grcode"      );
    	String              ss_year         = box.getString("s_gyear"       );
    	String              v_action        = box.getString("p_action"      );
    	int                 v_sulpapernum   = box.getInt   ("s_sulpapernum" );
    	
    	String 	v_gubun 	= box.getString("p_gubun");
    	int 	v_seq 		= box.getInt("p_seq");
    	String  v_poststr   = "";

    	String sql2 = "";
    	try { 
    		connMgr = new DBConnectionManager();
	    	if(v_gubun.equals("2")){
	    		sql2 = "select searchtext from tz_sulmembertarget where gubun = 2 and seq = ? ";
	    		// 직급
	            
	            pstmt = connMgr.prepareStatement(sql2);
	            pstmt.setInt   ( 1, v_seq  );
	            rs = pstmt.executeQuery();
	            if ( rs.next() ) { 
	            	v_poststr    = rs.getString("searchtext");
	            }
	            rs.close();
	    	}
			if(v_gubun.equals("1")) {	//조직
				sbSQL.append(" select a.userid, b.searchtext, a.name, a.hometel, a.handphone, a.ismailling, a.email 	\n")
					 .append(" from tz_member a, tz_sulmembertarget b										  			\n")
					 .append(" where b.gubun = '1'																		\n")
					 .append("  and  a.dept_cd = substr(b.searchtext, instr(b.searchtext,'|',1,2)+1,6)					\n")
					 .append(" 	and  b.seq = " + SQLString.Format(v_seq    ) + "           								\n")
					 .append(" order by a.userid 																		\n");
			} else if (v_gubun.equals("2")) {	//직급
				//System.out.println("v_poststr::>" +v_poststr);
				String[] post = v_poststr.split("[|]") ;
					
				
				System.out.println("post::>" +post);
				sbSQL.append(" select a.userid, b.searchtext, a.name, a.hometel, a.handphone, a.ismailling, a.email		\n")
					 .append(" from tz_member a, tz_sulmembertarget b 													\n")
					 .append(" where b.gubun = '2' 																		\n")
					 .append("  and  a.post in  (" );
				 for(int j =0 ; j < post.length ; j++){
					 //System.out.println(post[j]);
					 //System.out.println("post.length::>"+post.length);
					 if(post.length-1 == j){
						sbSQL.append("		 '"+post[j]+"'  " );
					 }else{
						sbSQL.append("		 '"+post[j]+"' ," );
						 
					 }
				 }
				 sbSQL.append(" ) \n")	
				 	  .append("  and  b.seq = " + SQLString.Format(v_seq    ) + "           								\n")
					  .append(" order by a.userid 																		\n");
				
				
			} else if (v_gubun.equals("3")) {	//직무
				sbSQL.append(" select a.userid, b.searchtext, a.name, a.hometel, a.handphone, a.ismailling, a.email		\n")
					 .append(" from tz_member a, tz_sulmembertarget b 													\n")
					 .append(" where b.gubun = '3' 																		\n")
					 .append("  and  a.job_cd = substr(searchtext, instr(searchtext,'|',1,3)+1,6) 						\n")
					 .append("  and  b.seq = " + SQLString.Format(v_seq    ) + "           								\n")
					 .append(" order by a.userid 																		\n");
			}else if (v_gubun.equals("4")) {	//과정
				sbSQL.append(" select a.userid, b.searchtext, a.name, a.hometel, a.handphone, a.ismailling, a.email		\n")
					 .append(" from tz_member a, tz_sulmembertarget b, tz_student c, tz_subj d, tz_subjseq e 			\n")
					 .append(" where a.userid = c.userid 																\n")
					 .append("  and  d.subj = e.subj 																	\n")
					 .append("  and  c.subj = d.subj 																	\n")
					 .append("  and  c.subjseq = e.subjseq																\n")
					 .append("  and  b.grcode = e.grcode																\n")
					 .append("  and  b.gubun = '4'																		\n")
					 .append(" 	and  b.seq = " + SQLString.Format(v_seq    ) + "           								\n")
					 .append(" 	and  e.subjseqgr = substr(b.searchtext, 1,4)           									\n")
					 .append(" 	and  d.upperclass = substr(b.searchtext, instr(b.searchtext,'|',1,1)+1,3)   			\n")
					 .append(" 	and  d.middleclass = substr(b.searchtext, instr(b.searchtext,'|',1,2)+1,3)  			\n")
					 .append(" 	and  d.lowerclass = substr(b.searchtext, instr(b.searchtext,'|',1,3)+1,3)				\n")
					 .append(" 	and  d.subj = substr(b.searchtext, instr(b.searchtext,'|',1,4)+1,5)						\n")
					 .append(" 	and  e.subjseq = substr(b.searchtext, instr(b.searchtext,'|',1,5)+1,4)					\n")
					 .append(" order by a.userid 																		\n");
			}
    			
				
    			System.out.println(this.getClass().getName() + "." + "selectSulmunMemberList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
    			
    			ls      = connMgr.executeQuery(sbSQL.toString());
    			
    			while ( ls.next() ) { 
    				dbox = ls.getDataBox();
    				
    				list.add(dbox);
    			}
    	} catch ( SQLException e ) {
    		ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
    		throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	} catch ( Exception e ) {
    		ErrorManager.getErrorStackTrace(e, box, "");
    		throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
    	} finally {
    		if ( ls != null ) { 
    			try { 
    				ls.close();  
    			} catch ( Exception e ) { } 
    		}
    		
    		if ( connMgr != null ) { 
    			try { 
    				connMgr.freeConnection(); 
    			} catch ( Exception e ) { } 
    		}
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
    	}
    	
    	return list;
    }
    /**
    설문 대상자 리스트
    @param box          receive from the form object and session
    @return ArrayList   설문 대상자
    */
    public ArrayList selectSulmunMemberList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ArrayList           list            = new ArrayList();
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              ss_grcode       = box.getString("s_grcode"      );
        String              ss_year         = box.getString("s_gyear"       );
        String              v_action        = box.getString("p_action"      );
        int                 v_sulpapernum   = box.getInt   ("s_sulpapernum" );

        try { 
            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();

                sbSQL.append(" select  a.subj                                                                       \n")
                     .append("     ,   a.year                                                                       \n")
                     .append("     ,   a.subjseq                                                                    \n")
                     .append("     ,   a.userid                                                                     \n")
                     .append("     ,   b.name                                                                       \n")
                     .append("     ,   b.email                                                                      \n")
                     .append("     ,   b.hometel                                                                    \n")
                     .append("     ,   b.handphone                                                                  \n")
                     .append("     ,   (                                                                            \n")
                     .append("          select  count(userid)                                                       \n")
                     .append("          from    tz_sulmailing                                                       \n")
                     .append("          where   subj            = 'TARGET'                                          \n")
                     .append("          and     grcode          = " + SQLString.Format(ss_grcode    ) + "           \n")
                     .append("          and     year            = " + SQLString.Format(ss_year      ) + "           \n")
                     .append("          and     sulpapernum     = " + SQLString.Format(v_sulpapernum) + "           \n")
                     .append("          and     userid          = a.userid                                          \n")
                     .append("         )       ismailcnt                                                            \n")
                     .append(" from    tz_sulmember     a                                                           \n")
                     .append("     ,   tz_member        b                                                           \n")
                     .append(" where   a.userid        = b.userid                                                   \n")
                     .append(" and     a.subj          = 'TARGET'                                                   \n")
                     .append(" and     a.grcode        = " + SQLString.Format(ss_grcode    ) + "                    \n")
                     .append(" and     a.year          = " + SQLString.Format(ss_year      ) + "                    \n")
                     .append(" and     a.sulpapernum   = " + SQLString.Format(v_sulpapernum) + "                    \n")
                     .append(" order by a.userid                                                                    \n");
                     
                System.out.println(this.getClass().getName() + "." + "selectSulmunMemberList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                ls      = connMgr.executeQuery(sbSQL.toString());
                
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();

                    list.add(dbox);
                }
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list;
    }
    /**
    설문 분류별 대상자 리스트
    @param box          receive from the form object and session
    @return ArrayList   설문 대상자
    */
    public ArrayList selectSulmunMemberBunruList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ArrayList           list            = new ArrayList();
        ListSet             ls              = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              ss_grcode       = box.getString("s_grcode"      );
        String              ss_year         = box.getString("s_gyear"       );
        String              v_action        = box.getString("p_action"      );
        int                 v_sulpapernum   = box.getInt   ("s_sulpapernum" );

        try { 
            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();

                /*sbSQL.append(" select  a.subj                                                                       \n")
                     .append("     ,   a.year                                                                       \n")
                     .append("     ,   a.subjseq                                                                    \n")
                     .append("     ,   a.userid                                                                     \n")
                     .append("     ,   b.name                                                                       \n")
                     .append("     ,   b.email                                                                      \n")
                     .append("     ,   b.hometel                                                                    \n")
                     .append("     ,   b.handphone                                                                  \n")
                     .append("     ,   (                                                                            \n")
                     .append("          select  count(userid)                                                       \n")
                     .append("          from    tz_sulmailing                                                       \n")
                     .append("          where   subj            = 'TARGET'                                          \n")
                     .append("          and     grcode          = " + SQLString.Format(ss_grcode    ) + "           \n")
                     .append("          and     year            = " + SQLString.Format(ss_year      ) + "           \n")
                     .append("          and     sulpapernum     = " + SQLString.Format(v_sulpapernum) + "           \n")
                     .append("          and     userid          = a.userid                                          \n")
                     .append("         )       ismailcnt                                                            \n")
                     .append(" from    tz_sulmember     a                                                           \n")
                     .append("     ,   tz_member        b                                                           \n")
                     .append(" where   a.userid        = b.userid                                                   \n")
                     .append(" and     a.subj          = 'TARGET'                                                   \n")
                     .append(" and     a.grcode        = " + SQLString.Format(ss_grcode    ) + "                    \n")
                     .append(" and     a.year          = " + SQLString.Format(ss_year      ) + "                    \n")
                     .append(" and     a.sulpapernum   = " + SQLString.Format(v_sulpapernum) + "                    \n")
                     .append(" order by a.userid                                                                    \n");*/
                sbSQL.append(" select  aa.grcode                                                                    								\n")
                     .append("     ,   aa.year                                                                     								\n")
                     .append("     ,   aa.gubun                                                                     								\n")
                     .append("     ,   aa.sulpapernum                                                               								\n")
                     .append("     ,   aa.seq                                                                       								\n")
                     .append("     ,   aa.searchtext                                                                								\n")
                     .append("     ,   bb.sulpapernm                                                                								\n")
                     .append("     ,   bb.sulstart                                                                  								\n")
                     .append("     ,   bb.sulend                                                                    								\n")
                     .append(" from    tz_sulmembertarget  aa, tz_sulpaper bb                                        \n")
                     .append(" where   aa.sulpapernum   = bb.sulpapernum								               \n")
                     .append(" and     bb.subj          = 'TARGET'       										   \n")
                     .append(" and     aa.grcode        = " + SQLString.Format(ss_grcode    ) + "                   \n")
                     .append(" and     aa.year          = " + SQLString.Format(ss_year      ) + "                   \n")
                     .append(" and     aa.sulpapernum   = " + SQLString.Format(v_sulpapernum) + "                   \n")
                     .append(" order by aa.seq	                                                                   \n");
                System.out.println(this.getClass().getName() + "." + "selectSulmunMemberBunruList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                ls      = connMgr.executeQuery(sbSQL.toString());
                
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();

                    list.add(dbox);
                }
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list;
    } 


    /**
    대상자를 찾기위한 회원 리스트
    @param box          receive from the form object and session
    @return ArrayList   대상자를 찾기위한 회원 리스트
    */
    public ArrayList selectMemberTargetList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";

        try { 
            String  ss_company  = box.getStringDefault("s_company", "ALL");      //  해당 회사의 comp code
            String  ss_gpm  = box.getStringDefault("s_gpm", "ALL");
            String  ss_dept  = box.getStringDefault("s_dept", "ALL");
            String  ss_part  = box.getStringDefault("s_part", "ALL");
            String  ss_jikwi  = box.getStringDefault("s_jikwi", "ALL");
            String  ss_jikun  = box.getStringDefault("s_jikun", "ALL");

            String ss_searchtype = box.getString("s_searchtype");
            String ss_searchtext = box.getString("s_searchtext");

            String v_action  = box.getString("p_action");

            list = new ArrayList();

    				String  v_orderColumn   = box.getString("p_orderColumn");           	// 정렬할 컬럼명
            String  v_orderType     = box.getString("p_orderType");           		// 정렬할 순서

            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();


                sql = "select a.userid,  ";
                sql += "       a.comp  asgn,  get_compnm(a.comp,2,2) companynm,   ";
                sql += "	   	  a.jikwi,       get_jikwinm(a.jikwi,a.comp) jikwinm    , get_deptnm(a.deptnam, a.userid) deptnam, ";
                sql += "	   	  a.cono,        a.name ";
                sql += "  from tz_member a  ";
                sql +="   where 1 = 1";

                if ( !ss_company.equals("ALL") ) { 
                    sql += " and a.comp like '" + GetCodenm.get_compval(ss_company) + "'";
                }
                if ( !ss_gpm.equals("ALL") ) { 
                    sql += " and a.comp like '" + GetCodenm.get_compval(ss_gpm) + "'";
                }
                if ( !ss_dept.equals("ALL") ) { 
                    sql += " and a.comp like '" + GetCodenm.get_compval(ss_dept) + "'";
                }
                if ( !ss_part.equals("ALL") ) { 
                    sql += " and a.comp like '" + GetCodenm.get_compval(ss_part) + "'";
                }
                if ( !ss_jikwi.equals("ALL") ) { 
                    sql += " and a.jikwi = " + SQLString.Format(ss_jikwi);
                }
                if ( !ss_jikun.equals("ALL") ) { 
                    sql += " and a.jikun = " + SQLString.Format(ss_jikun);
                }

                if ( ss_searchtype.equals("1") ) {  // 사번
                    sql += "  and a.cono like " + SQLString.Format("%" +ss_searchtext + "%");
                }
                else if ( ss_searchtype.equals("2") ) {  // id
                    sql += "  and a.userid like lower(" + SQLString.Format("%" +ss_searchtext + "%") + ")";
                }
                else if ( ss_searchtype.equals("3") ) {  // 성명
                    sql += "  and a.name like lower(" + SQLString.Format("%" +ss_searchtext + "%") + ")";
                }


                if ( v_orderColumn.equals("") ) { 
    				sql += " order by a.jikwi, a.name";
				} else { 
			    	sql += " order by " + v_orderColumn + v_orderType;
				}


                ls = connMgr.executeQuery(sql);// System.out.println(sql);
// Log.info.println(" >>  > " +sql);
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();

                    list.add(dbox);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    대상자설문  대상자 등록
    @param box          receive from the form object and session
    @return int
    */
    public int insertSulmunMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        String sql1 = "";
        String sql2 = "";
        int isOk = 0;

        String  ss_subj      = box.getString("s_subj");
        String ss_grcode    = box.getString("s_grcode");
        String  ss_year      = box.getString("s_gyear");
        String  ss_subjseq   = "0001";
        int v_sulpapernum    = box.getInt("s_sulpapernum");
        String  v_luserid   = box.getSession("userid");

        Vector  v_checks    = box.getVector("p_checks");
        String  v_userid  = "";
        int cnt = 0;
        int next = 0;

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1 = "select userid from TZ_SULMEMBER";
            sql1 +=  " where subj = ? and grcode = ? and year = ? and subjseq = ? and sulpapernum = ? and userid = ? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            sql2 =  "insert into TZ_SULMEMBER(subj, grcode, year, subjseq, sulpapernum, userid, luserid, ldate ) ";
            sql2 +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt2 = connMgr.prepareStatement(sql2);

            for ( int i = 0; i < v_checks.size(); i++ ) { 
                v_userid = (String)v_checks.elementAt(i);

                pstmt1.setString( 1, ss_subj);
                pstmt1.setString( 2, ss_grcode);
                pstmt1.setString( 3, ss_year);
                pstmt1.setString( 4, ss_subjseq);
                pstmt1.setInt( 5, v_sulpapernum);
                pstmt1.setString( 6, v_userid);

                try { 
                    rs = pstmt1.executeQuery();

                    if ( !rs.next() ) { 
                        pstmt2.setString( 1, ss_subj);
                        pstmt2.setString( 2, ss_grcode);
                        pstmt2.setString( 3, ss_year);
                        pstmt2.setString( 4, ss_subjseq);
                        pstmt2.setInt( 5, v_sulpapernum);
                        pstmt2.setString( 6, v_userid);
                        pstmt2.setString( 7, v_luserid);
                        pstmt2.setString( 8, FormatDate.getDate("yyyyMMddHHmmss") );

						isOk = pstmt2.executeUpdate();

						cnt += isOk;
                        next++;
					}
                } catch ( Exception ex ) { 
			            ErrorManager.getErrorStackTrace(ex);
                       throw new Exception(ex.getMessage() );
				}
                finally { if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }}
            }

            if ( next == cnt) { 
                connMgr.commit();
                isOk = cnt;
            } else { 
                connMgr.rollback();
                isOk = -1;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    대상자설문  대상자 삭제
    @param box          receive from the form object and session
    @return int
    */
    public int deleteSulmunBunruMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
		StringTokenizer st = null;

        //String  ss_subj      = box.getString("s_subj");
        //String ss_grcode    = box.getString("s_grcode");
        //String  ss_year      = box.getString("s_gyear");
        //String  ss_subjseq   = "0001";
        //int v_sulpapernum    = box.getInt("s_sulpapernum");
        String  v_duserid   = box.getSession("userid");

        Vector  v_checks    = box.getVector("p_checks");
        String  v_schecks = "";
		String  v_seq  = "";
        int cnt = 0;

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql =  " delete from TZ_SULMEMBERTARGET ";
            sql +=  " where seq     = ?  ";
            
            pstmt = connMgr.prepareStatement(sql);

            for ( int i = 0; i < v_checks.size(); i++ ) { 
                v_schecks = (String)v_checks.elementAt(i);
                st = new StringTokenizer(v_schecks,",");
                v_seq = (String)st.nextToken();

                pstmt.setString( 1, v_seq);

                isOk = pstmt.executeUpdate();
                cnt += isOk;
            }

            if ( v_checks.size() == cnt) { 
                connMgr.commit();
                isOk = cnt;
            } else { 
                connMgr.rollback();
                isOk = 0;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    /**
    대상자설문  대상자 삭제
    @param box          receive from the form object and session
    @return int
    */
    public int deleteSulmunMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
		StringTokenizer st = null;

        String  ss_subj      = box.getString("s_subj");
        String ss_grcode    = box.getString("s_grcode");
        String  ss_year      = box.getString("s_gyear");
        String  ss_subjseq   = "0001";
        int v_sulpapernum    = box.getInt("s_sulpapernum");
        String  v_duserid   = box.getSession("userid");

        Vector  v_checks    = box.getVector("p_checks");
        String  v_schecks = "";
		String  v_userid  = "";
        int cnt = 0;

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql =  " delete from TZ_SULMEMBER ";
            sql +=  " where subj     = ?  ";
            sql +=  "   and grcode       = ?  ";
            sql +=  "   and year       = ?  ";
            sql +=  "   and subjseq       = ?  ";
            sql +=  "   and sulpapernum= ?  ";
            sql +=  "   and userid       = ?  ";

            pstmt = connMgr.prepareStatement(sql);

            for ( int i = 0; i < v_checks.size(); i++ ) { 
                v_schecks = (String)v_checks.elementAt(i);
                st = new StringTokenizer(v_schecks,",");
                v_userid = (String)st.nextToken();

                pstmt.setString( 1, ss_subj);
                pstmt.setString( 2, ss_grcode);
                pstmt.setString( 3, ss_year);
                pstmt.setString( 4, ss_subjseq);
                pstmt.setInt( 5, v_sulpapernum);
                pstmt.setString( 6, v_userid);

                isOk = pstmt.executeUpdate();
                cnt += isOk;
            }

            if ( v_checks.size() == cnt) { 
                connMgr.commit();
                isOk = cnt;
            } else { 
                connMgr.rollback();
                isOk = 0;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    *  대상자 설문지 셀렉트박스 (RequestBox, 셀렉트박스명,선택값,이벤트명)
	*  TZ_SULPAPER 이용
    */
    public static String getSulpaperSelect (String p_grcode, String p_gyear, String p_subj, String name, int selected, String event) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        DataBox     dbox = null;

        result = "  <SELECT name=" + name + " " + event + " > \n";

        result += " <option value='0' > 설문지를 선택하세요.</option > \n";

        try { 
            connMgr = new DBConnectionManager();

			sql = "select grcode,       subj,         ";
            sql += "       sulpapernum,  sulpapernm, year, ";
            sql += "       totcnt,       sulnums, sulmailing, sulstart, sulend, ";
            sql += "       'TARGET'      subjnm ";
            sql += "  from tz_sulpaper ";
            sql += " where grcode = " + StringManager.makeSQL(p_grcode);
            sql += "   and subj   = " +StringManager.makeSQL(p_subj);
            sql += "   and year   = " + StringManager.makeSQL(p_gyear);
            sql += " order by subj, sulpapernum asc";

            ls = connMgr.executeQuery(sql);

                String v_null_test = "";
                String v_subj_bef = "";

            while ( ls.next() ) { 

                    dbox = ls.getDataBox();

                result += " <option value=" + dbox.getInt("d_sulpapernum");
                if ( selected == dbox.getInt("d_sulpapernum") ) { 
                    result += " selected ";
                }

                result += " > " + dbox.getString("d_sulpapernm") + "</option > \n";
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

    /**
    *  대상자 설문지 셀렉트박스 (RequestBox, 셀렉트박스명,선택값,이벤트명)
	*  TZ_SULPAPER 이용
    */
    public static String getSulpaperSelect2 (String p_grcode, String p_gyear, String p_subj, String name, int selected, String event) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        DataBox     dbox = null;

        result = "  <SELECT name=" + name + " " + event + " > \n";

        result += " <option value='0' > 설문지를 선택하세요.</option > \n";

        try { 
            connMgr = new DBConnectionManager();

			sql = "select grcode,       subj,         ";
            sql += "       sulpapernum,  sulpapernm, year, ";
            sql += "       totcnt,       sulnums, sulmailing, sulstart, sulend, ";
            sql += "       'TARGET'      subjnm ";
            sql += "  from tz_sulpaper ";
            sql += " where grcode = " + StringManager.makeSQL(p_grcode);
            sql += "   and subj   = " +StringManager.makeSQL(p_subj);
            sql += "   and year   = " + StringManager.makeSQL(p_gyear);
            // sql += "   and sulmailing != 'N' ";
            sql += " order by subj, sulpapernum asc";

            ls = connMgr.executeQuery(sql);

                String v_null_test = "";
                String v_subj_bef = "";

            while ( ls.next() ) { 

                    dbox = ls.getDataBox();

                result += " <option value=" + dbox.getInt("d_sulpapernum");
                if ( selected == dbox.getInt("d_sulpapernum") ) { 
                    result += " selected ";
                }

                result += " > " + dbox.getString("d_sulpapernm") + "</option > \n";
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


    /**
    *  설문메일보내기 정보 등록
	*  tz_sulmailing 이용
    */
    public int insertSulmunMailing(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet rs = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk = 0;
		StringTokenizer st = null;

        String v_grcode = box.getString("p_grcode");
        int v_sulpapernum   = box.getInt("p_sulpapernum");
        String v_luserid    = box.getSession("userid");
        Vector v_checks     = box.getVector("p_checks");    // userid, subj, gyear,subjseq
        String v_schecks = "";
        String v_userid = "";
        String v_subj   = "";
        String v_gyear  = "";
        String v_subjseq= "";
        String v_ismailcnt= "";
        String v_sulpapernm = "";

        int cnt  = 0;
        int next = 0;

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 설문명
            sql2 = " select sulpapernm                  \n" + 
                   " from   tz_sulpaper                 \n" + 
                   " where  grcode      = ?             \n" + 
                   " and    year        = ?             \n" + 
                   " and    subj        = 'TARGET'      \n" + 
                   " and    sulpapernum = ?             \n" ; 
            
            pstmt2 = connMgr.prepareStatement(sql2);
            
            pstmt2.setString( 1, v_grcode       );
            pstmt2.setString( 2, v_gyear        );
            pstmt2.setInt   ( 3, v_sulpapernum  );
            rs = pstmt2.executeQuery();
            if ( rs.next() ) { 
                v_sulpapernm    = rs.getString("sulpapernm");
            }
            rs.close();


            // tz_sulmailing(subj, grcode, year, subjseq, sulpapernum, userid, sulpapernm, name, companynm, deptnm, jikwinm, return ,email, luserid)
            sql1 =  " insert into tz_sulmailing  ";
            sql1 +=  " select ?, ?, ?, ?, ?, ?,  ?, ";
            sql1 +=  " name, get_compnm(comp,2,2) companynm, '' deptnm, '' jikwinm, 'N', email, ? ";
            sql1 +=  " from tz_member where userid=? ";
            
            pstmt1 = connMgr.prepareStatement(sql1);

            sql3 =  " update tz_sulmailing  set ";
            sql3 +=  "   return  = 'N'";
            sql3 +=  " where userid=? ";
            sql3 +=  " and grcode=? ";
            sql3 +=  " and year=? ";
            sql3 +=  " and subj='TARGET' ";
            sql3 +=  " and subjseq=? ";
            sql3 +=  " and sulpapernum=? ";
            
            pstmt3 = connMgr.prepareStatement(sql3);

            for ( int i = 0; i < v_checks.size(); i++ ) { 
                v_schecks = (String)v_checks.elementAt(i);
                st = new StringTokenizer(v_schecks,",");
                v_userid  = (String)st.nextToken();
                v_subj    = (String)st.nextToken();
                v_gyear   = (String)st.nextToken();
                v_subjseq = (String)st.nextToken();
                v_ismailcnt = (String)st.nextToken();

                // tz_sulmailing 테이블에 없는 경우..
                if ( Integer.parseInt(v_ismailcnt)<1) { 
                    pstmt1.setString( 1, v_subj);
                    pstmt1.setString( 2, v_grcode);
                    pstmt1.setString( 3, v_gyear);
                    pstmt1.setString( 4, v_subjseq);
                    pstmt1.setInt(    5, v_sulpapernum);
                    pstmt1.setString( 6, v_userid);
                    pstmt1.setString( 7, v_sulpapernm);
                    pstmt1.setString( 8, v_luserid);
                    pstmt1.setString( 9, v_userid);
    				isOk = pstmt1.executeUpdate();
    		    } else { 
                    pstmt3.setString( 1, v_userid);
                    pstmt3.setString( 2, v_grcode);
                    pstmt3.setString( 3, v_gyear);
                    pstmt3.setString( 4, v_subjseq);
                    pstmt3.setInt(    5, v_sulpapernum);
    				isOk = pstmt3.executeUpdate();
    		    }
            }

            if ( isOk > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
                isOk = 0;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    대상자설문  대상자 등록
    @param box          receive from the form object and session
    @return int
    */
    public int insertSulmunBunruMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        ListSet             ls      = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        int isOk = 0;

        //String  ss_subj      = box.getString("s_subj");
        String ss_grcode    = box.getString("s_grcode");
        String  ss_year      = box.getString("s_gyear");
        String  ss_gubun      = box.getString("pp_gubun");	//1. 조직, 2.직급, 3.직무, 4.과정
        //String  ss_subjseq   = "0001";
        int v_sulpapernum    = box.getInt("s_sulpapernum");
        String  v_luserid   = box.getSession("userid");

        //Vector  v_checks    = box.getVector("p_checks");
        //String  v_userid  = "";
        String v_searchtext = box.getString("p_searchtext");
        int v_cnt = 0;
        int cnt = 0;
        int next = 0;
        

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            //sql1 = "select userid from TZ_SULMEMBER";
            //sql1 +=  " where subj = ? and grcode = ? and year = ? and subjseq = ? and sulpapernum = ? and userid = ? ";
            //pstmt1 = connMgr.prepareStatement(sql1);

            //sql2 =  "insert into TZ_SULMEMBER(subj, grcode, year, subjseq, sulpapernum, userid, luserid, ldate ) ";
            //sql2 +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";
            //pstmt2 = connMgr.prepareStatement(sql2);
            
            // ----------------------   게시판 번호 가져온다 ----------------------------
            sql = "select nvl(max(seq), 0) from TZ_SULMEMBERTARGET";
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_seq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------
            
            sql1  =  "select count(*) as cnt from TZ_SULMEMBERTARGET ";
            sql1 +=  " where grcode = ? and year = ? and gubun = ? and sulpapernum = ? and searchtext = ?";
            pstmt1 = connMgr.prepareStatement(sql1);
            
            sql2 =  "insert into TZ_SULMEMBERTARGET(grcode, year, gubun, seq, sulpapernum, searchtext, luserid, ldate ) ";
            sql2 +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt2 = connMgr.prepareStatement(sql2);
                //v_userid = (String)v_checks.elementAt(i);

            
            pstmt1.setString( 1, ss_grcode);
            pstmt1.setString( 2, ss_year);
            pstmt1.setString( 3, ss_gubun);
            pstmt1.setInt( 4, v_sulpapernum);        
            pstmt1.setString( 5, v_searchtext);        

            try { 
                rs = pstmt1.executeQuery();
                if ( rs.next() ) { 
                    v_cnt    = rs.getInt("cnt");
                }
                rs.close();
                //if ( !rs.next() ) { 
                if ( v_cnt == 0 ) { 
                    pstmt2.setString( 1, ss_grcode);
                    pstmt2.setString( 2, ss_year);
                    pstmt2.setString( 3, ss_gubun);
                    pstmt2.setInt( 4, v_seq);
                    pstmt2.setInt( 5, v_sulpapernum);
                    pstmt2.setString( 6, v_searchtext);
                    pstmt2.setString( 7, v_luserid);
                    pstmt2.setString( 8, FormatDate.getDate("yyyyMMddHHmmss") );

					isOk = pstmt2.executeUpdate();

					cnt += isOk;
                    next++;
				}
            } catch ( Exception ex ) { 
		            ErrorManager.getErrorStackTrace(ex);
                   throw new Exception(ex.getMessage() );
			}
            finally { 
            	if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            }

            if ( next == cnt) { 
                connMgr.commit();
                isOk = cnt;
            } else { 
                connMgr.rollback();
                isOk = -1;
            }
        } catch ( Exception ex ) { 
            isOk = 0;
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
}