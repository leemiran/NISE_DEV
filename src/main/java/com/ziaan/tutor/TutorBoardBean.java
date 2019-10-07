// **********************************************************
//  1. 제      목: 강사 자유게시판
//  2. 프로그램명: BoardBean.java
//  3. 개      요: 강사 자유게시판
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 김미향 2008.12.11
//  7. 수      정:
// **********************************************************
package com.ziaan.tutor;

import java.sql.PreparedStatement;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class TutorBoardBean { 
    private ConfigSet config;

    public TutorBoardBean() { 
        try { 
            config = new ConfigSet();
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
     * 강사 자유게시판 생성
     * @param box
     * @return int v_tabseq
     * @throws Exception
     */
    public int InsertBds(RequestBox box) throws Exception {
    	DBConnectionManager connMgr		= null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        ListSet             ls          = null;
        int					v_tabseq    = 0;
        int					v_cnt		= 0;
        
        try { 
			connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.setLength(0);
            sbSQL.append(" select count(*) as cnt ");
            sbSQL.append(" from   tz_bds ");
            sbSQL.append(" where  type = 'TUBD'");
            ls              = connMgr.executeQuery(sbSQL.toString());
            if ( ls.next() ) { 
            	v_cnt    = ls.getInt(1);
            }
            
            if (v_cnt > 0) {
                
                sbSQL.setLength(0);
                sbSQL.append(" select tabseq ");
                sbSQL.append(" from   tz_bds ");
                sbSQL.append(" where  type = 'TUBD'");
                ls              = connMgr.executeQuery(sbSQL.toString());
                if ( ls.next() ) { 
                	v_tabseq    = ls.getInt(1);
                }
                
            } else {

            	sbSQL.setLength(0);
	            sbSQL.append(" select nvl(max(tabseq), 0) + 1 from tz_bds ");
	            ls              = connMgr.executeQuery(sbSQL.toString());
	
	            if ( ls.next() ) { 
	                v_tabseq    = ls.getInt(1);
	            } else { 
	                v_tabseq    = 1;
	            }
	            
	            // StringBuffer 초기화    
	            sbSQL.setLength(0);
	
	            // insert tz_bds table
	            sbSQL.append(" insert into tz_bds                               \n")
	                 .append(" (                                                \n")
	                 .append("         tabseq  , type  , grcode    , comp       \n")
	                 .append("     ,   subj    , year  , subjseq   , sdesc      \n")
	                 .append("     ,   ldesc   , status, luserid   , ldate      \n")
	                 .append(" ) values (                                       \n")
	                 .append("         ?       , ?     , ?         , ?          \n")
	                 .append("     ,   ?       , ?     , ?         , ?          \n")
	                 .append("     ,   ?       , ?     , ?         , ?          \n")
	                 .append(" )                                                \n");
	            
	            pstmt           = connMgr.prepareStatement(sbSQL.toString());
	
	            pstmt.setInt   ( 1, v_tabseq    );
	            pstmt.setString( 2, "TUBD"      ); // 게시판 타입
	            pstmt.setString( 3, "0000000"   );
	            pstmt.setString( 4, "0000000000");
	            pstmt.setString( 5, "00000"     );
	            pstmt.setString( 6, "0000"      );
	            pstmt.setString( 7, "0000"      );
	            pstmt.setString( 8, "강사 자유게시판");
	            pstmt.setString( 9, ""          );
	            pstmt.setString(10, "A"         );
	            pstmt.setString(11, "SYSTEM"    );
	            pstmt.setString(12, FormatDate.getDate("yyyyMMddHHmmss") );
	
	            isOk = pstmt.executeUpdate();

	            if (isOk == 1) {
	                connMgr.commit();
	            } else {
	                connMgr.rollback();
	            }
                
	        }
        } catch ( Exception e ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e1 ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	}catch (Exception e10) {} 
            }            
        }
        
        return v_tabseq;
    }

}