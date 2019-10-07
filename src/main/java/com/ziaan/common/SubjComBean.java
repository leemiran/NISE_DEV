// **********************************************************
//  1. 제      목: 
//  2. 프로그램명: SubjComBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.3
//  5. 버      젼: 0.1
//  6. 작      성: 이창훈 2005. 7. 16
//  7. 수      정:
// **********************************************************
package com.ziaan.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class SubjComBean { 
	
	public SubjComBean() { }

    public boolean IsMasterPropose(String p_master, String p_userid) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        boolean isMasterPropose = true;
        
        String sql  = "";
        
        try { 
        	sql += " select \n";
            sql += "   count(subjseq) CNTS\n";
            sql += " from \n";
            sql += "   vz_mastersubjseq a\n";
            sql += " where\n";
            sql += "   a.mastercd = '" +p_master + "'\n";
            sql += "   and (select 'Y' from tz_propose where userid = '" +p_userid + "' and subj= a.scsubj and subjseq = a.scsubjseq and year=a.year and (isproposeapproval != 'N'and chkfinal != 'N') ) = 'Y'\n";
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
              if ( ls.getInt("CNTS") > 0) { 
                isMasterPropose = false;
              } else { 
              }
            }
        }

        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isMasterPropose;
    }
    
    
    public String IsEduTargetUserid(String p_mastercd, String p_userid) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        String errCode = "0";
        
        try { 
          errCode = IsEduTargetUserid(connMgr, p_mastercd, p_userid);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return errCode;
    }
    
    public String IsEduTargetUserid(DBConnectionManager connMgr, String p_mastercd, String p_userid) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String errCode = "0";
        
        String sql  = "";
        
        try { 
        	sql  = " select \n";
        	sql += "   count(userid) CNTS            \n";
        	sql += " from                            \n";
        	sql += "   tz_edutarget                  \n";
        	sql += " where  \n";
        	sql += "   userid   = " +SQLString.Format(p_userid) + "\n";
        	sql += "   and mastercd = " +SQLString.Format(p_mastercd) + "\n";

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
              if ( ls.getInt("CNTS") > 0) { 
                errCode = "36";
              }
            }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return errCode;
    }
    
    
    
    
    public boolean IsEduTarget(String p_subj, String p_userid, String p_gyear) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        boolean isEduTarget = false;
        
        try { 
          isEduTarget = IsEduTarget(connMgr, p_subj, p_userid, p_gyear);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isEduTarget;
    }
    
    public boolean IsEduTarget(DBConnectionManager connMgr, String p_subj, String p_userid, String p_gyear) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        boolean isEduTarget = false;
        
        String sql  = "";
        
        try { 
        	sql  = " select \n";
        	sql += "   count(userid) CNTS            \n";
        	sql += " from                            \n";
        	sql += "   tz_edutarget a, tz_mastersubj b \n";
        	sql += " where b.subjcourse  = " + SQLString.Format(p_subj) + "   \n";
        	sql += "   and a.userid  = " +SQLString.Format(p_userid) + "  \n";
        	sql += "   and a.mastercd = b.mastercd \n";
        	sql += "   and a.gyear = " +SQLString.Format(p_gyear) + " \n";

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
              if ( ls.getInt("CNTS") > 0) { 
                isEduTarget = true;
              }
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return isEduTarget;
    }
    
    
        public boolean IsEduTarget(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        boolean isEduTarget = false;
        
        try { 
        
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isEduTarget;
    }
    
    
    public boolean IsEduTarget(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
    	
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        boolean isEduTarget = false;
        
        String sql  = "";
        
        try { 
        	sql  = " select \n";
        	sql += "   count(userid) CNTS            \n";
        	sql += " from                            \n";
        	sql += "   tz_edutarget a, tz_mastersubj b \n";
        	sql += " where b.subjcourse  = " + SQLString.Format(p_subj) + "   \n";
        	sql += "   and b.subjseq     = " + SQLString.Format(p_subjseq) + "  \n";
        	sql += "   and b.year        = " + SQLString.Format(p_year) + "   \n";
        	sql += "   and a.userid      = " + SQLString.Format(p_userid) + "  \n";
        	sql += "   and a.mastercd    = b.mastercd \n";
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
              if ( ls.getInt("CNTS") > 0) { 
                isEduTarget = true;
              } else { 
              }
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        return isEduTarget;
    }
    
    
    
    public boolean IsMasterSubj(String p_subj, String p_gyear) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        boolean isMasterSubj = false;
        
        String sql  = "";
        
        try { 
        	sql  = " select \n";
        	sql += "   count(subjseq) CNTS            \n";
        	sql += " from                            \n";
        	sql += "   vz_mastersubjseq where subj = " +StringManager.makeSQL(p_subj) + " and gyear = " +StringManager.makeSQL(p_gyear);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
              if ( ls.getInt("CNTS") > 0) { 
                isMasterSubj = true;
              }
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isMasterSubj;
    }
    
    
    
    
    public boolean IsMasterSubj(String p_subj, String p_year, String p_subjseq) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        boolean isMasterSubj = false;
        
        String sql  = "";
        
        try { 
        	sql  = " select \n";
        	sql += "   count(subjseq) CNTS            \n";
        	sql += " from                            \n";
        	sql += "   tz_mastersubj   \n";
        	sql += " where subjcourse  = " + SQLString.Format(p_subj) + "   \n";
        	sql += "   and subjseq = " + SQLString.Format(p_subjseq) + "  \n";
        	sql += "   and year    = " + SQLString.Format(p_year) + "   \n";
            
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
              if ( ls.getInt("CNTS") > 0) { 
                isMasterSubj = true;
              }
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isMasterSubj;
    }
    
    public String getMasterCode(String p_subj, String p_year, String p_subjseq) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String     mastercode     = "";
        
        String sql  = "";
        
        try { 
        	sql  = " select            \n";
        	sql += "   mastercd        \n";
        	sql += " from              \n";
        	sql += "   tz_mastersubj   \n";
        	sql += " where subjcourse  = " + SQLString.Format(p_subj) + "   \n";
        	sql += "   and subjseq = " + SQLString.Format(p_subjseq) + "  \n";
        	sql += "   and year    = " + SQLString.Format(p_year) + "   \n";
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                mastercode = ls.getString("mastercd");
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return mastercode;
    }
    
    
    public boolean IsPropose(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	connMgr = new DBConnectionManager();
    	
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        boolean isPropose = false;
        int v_propCnt = 0;
        
        String sql  = "";
        
        try { 
        	sql  = " select \n";
        	sql += "   count(userid) CNTS\n";
        	sql += " from \n";
        	sql += "   tz_propose\n";
        	sql += " where subj  = " + SQLString.Format(p_subj) + "   \n";
        	sql += "   and subjseq = " + SQLString.Format(p_subjseq) + "  \n";
        	sql += "   and year    = " + SQLString.Format(p_year) + "   \n";
        	sql += "   and userid  = " + SQLString.Format(p_userid) + "  \n";
        	sql += "   and to_char(sysdate,'YYYYMMDDHH24') between b.propstart and b.propend \n";
            sql += "   and a.chkfinal = 'Y'                              \n";
            
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
              if ( ls.getInt("CNTS") > 0) { 
                isPropose = true;
              }
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isPropose;
    }
    
    
    
    //// //// //// //// //// //// //// //// //// //// //// ///현재교육기간별 리턴value//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    // @return value
    // 0: 교육일 정보 미입력
    // 1: 수강신청전
    // 2: 수강신청기간
    // 3: 운영자승인기간
    // 4: 학습기간
    // 5: 학습종료
    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 

    public String getEduTerm(String p_subj, String p_subjseq, String p_year) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String v_eduterm = "";
        try { 
            connMgr = new DBConnectionManager();
            v_eduterm = getEduTerm(connMgr,p_subj, p_subjseq, p_year);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_eduterm;
    }


    
    public String getEduTerm(DBConnectionManager connMgr, String p_subj, String p_subjseq, String p_year) throws Exception { 
        ListSet         ls          = null;
        StringBuffer    sbSQL       = new StringBuffer("");
        
        int             iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String          v_eduterm   = "";
        String          v_isclosed  = "";
                        
        int             v_today     = 0;
        int             v_propstart = 0;
        int             v_propend   = 0;
        int             v_edustart  = 0;
        int             v_eduend    = 0;

        try { 
            sbSQL.append(" select  to_char(sysdate, 'YYYYMMDDHH') today                         \n")
                 .append("     ,   propstart                                                    \n")
                 .append("     ,   propend                                                      \n")
                 .append("     ,   edustart                                                     \n")
                 .append("     ,   eduend                                                       \n")
                 .append("     ,   isclosed                                                     \n")
                 .append(" from    tz_subjseq                                                   \n")
                 .append(" where   subj        = " + SQLString.Format(p_subj      ) + "         \n")
                 .append(" and     subjseq     = " + SQLString.Format(p_subjseq   ) + "         \n")
                 .append(" and     year        = " + SQLString.Format(p_year      ) + "         \n");
                 
//            System.out.println(this.getClass().getName() + "." + "getEduTerm() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls  = connMgr.executeQuery(sbSQL.toString());

            if ( ls.next() ) { 
                v_today         = ls.getInt("today"     );
                v_propstart     = ls.getInt("propstart" );
                v_propend       = ls.getInt("propend"   );
                v_edustart      = ls.getInt("edustart"  );
                v_eduend        = ls.getInt("eduend"    );
                
                if ( !(v_propstart == 0) && !(v_propend == 0) ) { 
                	if ( v_edustart <= v_today     && v_eduend   > v_today )  		// 교육기간
                		v_eduterm   = "4";           
                	else if ( v_eduend <= v_today )                               // 교육종료후
                		v_eduterm   = "5";            
                	else if ( v_today < v_propstart   )                            // 수강신청전
                        v_eduterm   = "1";           
                    else if ( v_propstart <= v_today    && v_propend  > v_today )  // 수강신청기간
                        v_eduterm   = "2";           
                    else if ( v_propend <= v_today      && v_edustart > v_today )  // 교육대기기간
                        v_eduterm   = "3";           
                } else { 
                    if ( (v_edustart == 0) && (v_eduend == 0) )  					
                    	v_eduterm   = "0";
                    else if ( v_edustart <= v_today     && v_eduend   > v_today )  	// 교육기간
                        v_eduterm   = "4";           
                    else if ( v_eduend <= v_today )                                // 교육종료후
                        v_eduterm   = "5";
                    else
                    	v_eduterm   = "0";
                }
                
            }
            
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
        
        return v_eduterm;
    }
    
    
    public String getCpApproval(String p_subj) throws Exception { 
     	DBConnectionManager connMgr     = null;
        ListSet             ls      = null;
        String v_cpapproval = "";
        String sql  = "";

        try { 
        	connMgr = new DBConnectionManager();
        	sql = "select cpapproval from tz_subj where subj = " +StringManager.makeSQL(p_subj);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
            	v_cpapproval = ls.getString("cpapproval");
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_cpapproval;
    }
    
    
    
    public String getEduTermClosedGubun(String p_subj, String p_subjseq, String p_year) throws Exception {
        DBConnectionManager connMgr = null;
        String v_eduterm = "";
        try {
            connMgr = new DBConnectionManager();
            v_eduterm = getEduTermClosedGubun(connMgr,p_subj, p_subjseq, p_year);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return v_eduterm;
    }
    
    
    public String getEduTermClosedGubun(DBConnectionManager connMgr, String p_subj, String p_subjseq, String p_year) throws Exception {
        ListSet ls = null;
        String v_eduterm = "";
        String v_isclosed = "";

        int    v_today = 0;
        int    v_propstart = 0;
        int    v_propend = 0;
        int    v_edustart = 0;
        int    v_eduend = 0;

        String sql  = "";

        try {
        	//sql  = "select count(seq) cnt from tz_approval ";
        	//sql += " where workchief  = " + SQLString.Format(p_userid);
        	//sql += " and gubun = '2' and ispropose = 'N'" ;
        	//sql += " and userid  = " + SQLString.Format(p_userid);
        	sql+= " select    \n";
            sql+= "   to_char(sysdate, 'YYYYMMDDHH') today, \n";
            sql+= "   propstart, \n";
            sql+= "   propend,\n";
            sql+= "   edustart,\n";
            sql+= "   eduend,\n";
            sql+= "   isclosed\n";
            sql+= " from \n";
            sql+= "   tz_subjseq\n";
            sql+= " where \n";
            sql+= "  subj = '"+p_subj+"'\n";
            sql+= "  and subjseq ='"+p_subjseq+"'\n";
            sql+= "  and year = '"+p_year+"'\n";

            //System.out.println("eduterm+++++++++++++++++++++++++++++++++++++++++++++++++===>>>>"+sql);
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
            	v_today     = ls.getInt("today");
            	v_propstart = ls.getInt("propstart");
            	v_propend   = ls.getInt("propend");
            	v_edustart  = ls.getInt("edustart");
            	v_eduend    = ls.getInt("eduend");
            	v_isclosed  = ls.getString("isclosed");
                

                if(!(v_edustart==0) && !(v_eduend==0)){
                  if(v_edustart>v_today){
                    v_eduterm = "3";           //교육대기기간
                  }else if(v_edustart<=v_today&&v_eduend>=v_today){
                    v_eduterm = "4";           //교육기간
                  }else if(v_eduend<v_today){ 
                    if(v_isclosed.equals("N")){
                      v_eduterm = "5";            //교육종료후 before Closed
                    }else{
                      v_eduterm = "6";            //교육종료후 after Closed
                    }
                  }
                }else{
                  v_eduterm = "0";
                }
            }
           // System.out.println("eduterm+++++++++++++++++++++++++++++++++++++++++++++++++===>>>>"+v_eduterm);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return v_eduterm;
    }

}