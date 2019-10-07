// **********************************************************
//  1. 제      목: 마일리지 관리
//  2. 프로그램명 : MileageManager.java
//  3. 개      요: 마일리지 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7.  2
//  7. 수      정:
// **********************************************************

package com.ziaan.common    ;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * 마일리지 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class MileageManager { 

    public MileageManager() { }


    /**
    * 마일리지 기본점수
    * @param  code        마일리지 코드
    * @return result      기본점수
    * @throws Exception
    */
    public static int getMileageBase (String code) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select nvl(base,0) base from TZ_MILEAGE        ";
            sql += "  where mileagecode = " + StringManager.makeSQL(code); 
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("base");
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
     * 마일리지 기본점수
     * @param  code        마일리지 코드
     * @return result      기본점수
     * @throws Exception
     */
     public static int getMemberMileagePoint (String v_userid) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet             ls      = null;
         String              sql     = "";
         int result = 0;

         try { 
             connMgr = new DBConnectionManager();

             sql  = " select MileagePoint from TZ_Member               ";
             sql += " where userid = " + StringManager.makeSQL(v_userid); 
             ls = connMgr.executeQuery(sql);

             if ( ls.next() ) { 
                 result = ls.getInt("mileagepoint");
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
    * 마일리지 유효성 체크
    * @param  code        마일리지 코드
    * @return result      0 : 해당 마일리지 사용안함    1 : 해당마일리지 사용함
    * @throws Exception
    */
    public static int getMileageUse (String code) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String today = FormatDate.getDate("yyyyMMdd");
        int result = 0;


        try { 
            connMgr = new DBConnectionManager();

            sql  = " select count(*) cnt from TZ_MILEAGE        ";
            sql += "  where mileagecode  = " + StringManager.makeSQL(code); 
            sql += "    and isuse    = 'Y'                      ";
            sql += "    and started <= " + StringManager.makeSQL(today); 
            sql += "    and ended   >= " + StringManager.makeSQL(today); 
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
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
    * 마일리지 저장
    * @param  code        마일리지 코드
    * @param  userid      USER ID
    * @return result      0 : insert or update success  1 : insert or update fail
    * @throws Exception
    */
     public static int insertMileage1(String code, String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ListSet             ls      = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        int cnt   = 0;
        int isOk1 = 0;
        int isOk2 = 0;

        isOk1 = MileageManager.getMileageUse(code);
        
        System.out.println("isOk1" + isOk1);
        
        if ( isOk1 > 0 ) { 
            int v_mileagebase  = MileageManager.getMileageBase(code);

            try { 
                connMgr = new DBConnectionManager();

                sql  = "select count(*) from TZ_MEMBER_MILEAGE  ";
                sql += " where userid      = " + StringManager.makeSQL(userid);
                sql += "   and mileagecode = " + StringManager.makeSQL(code);
                
                System.out.println("sql : " + sql);
                
                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) { 
                    cnt = ls.getInt(1);
                }
                ls.close();

                if ( cnt > 0 ) {                      // UPDATE
                    sql1  = " update TZ_MEMBER_MILEAGE set point = point + ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
                    sql1 += " where userid      = ? ";
                    sql1 += "   and mileagecode = ? ";
                    
                    System.out.println("sql : " + sql1);
                    
                    pstmt1 = connMgr.prepareStatement(sql1);

                    pstmt1.setInt(1, v_mileagebase);
                    pstmt1.setString(2, userid);
                    pstmt1.setString(3, code);

                    isOk2 = pstmt1.executeUpdate();
                    
                    if ( pstmt1 != null ) { pstmt1.close(); }
                    
                } else {                            // INSERT
                    sql2  = " insert into TZ_MEMBER_MILEAGE(userid, mileagecode, point, ldate)      ";
                    sql2 += "                 values(?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    
                    System.out.println("sql : " + sql2);
                    
                    pstmt2 = connMgr.prepareStatement(sql2);

                    pstmt2.setString(1, userid);
                    pstmt2.setString(2, code);
                    pstmt2.setInt(3, v_mileagebase);

                    isOk2 = pstmt2.executeUpdate();
                    
                    if ( pstmt2 != null ) { pstmt2.close(); }
                }
            }
            catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
                if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }

        }
        return isOk1 * isOk2;
    }

     
    /**
     * 마일리지 저장
     * @param  code        마일리지 코드
     * @param  userid      USER ID
     * @return result      0 : insert or update success  1 : insert or update fail
     * @throws Exception
     */
    public static int insertMileage(String v_code, String v_userid) throws Exception {
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;

        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        int                 iIdxAdd         = 0;
        
        int                 cnt             = 0;
        int                 isOk1           = MileageManager.getMileageUse(v_code);
        int                 isOk2           = 0;
        int                 v_mileagebase   = 0;
        
        if ( isOk1 == 0 )
            return isOk1;
        
        v_mileagebase                       = MileageManager.getMileageBase(v_code);
        
        if ( v_mileagebase == 0 )
            v_mileagebase                   = -1 * MileageManager.getMemberMileagePoint(v_userid);
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" INSERT INTO Tz_Member_Mileage                                                                                            \n")
                 .append(" (                                                                                                                        \n")
                 .append("         UserId                                                                                                           \n")
                 .append("     ,   MileageSeq                                                                                                       \n")
                 .append("     ,   MileageCode                                                                                                      \n")
                 .append("     ,   Point                                                                                                            \n")
                 .append("     ,   LUserId                                                                                                          \n")
                 .append("     ,   LDate                                                                                                            \n")
                 .append(" ) VALUES (                                                                                                               \n")
                 .append("         ?                                                                                                                \n")
                 .append("     ,   (SELECT NVL(MAX(MileageSeq) + 1, 1) FROM Tz_Member_Mileage WHERE UserId = " + SQLString.Format(v_userid) + ")    \n")
                 .append("     ,   ?                                                                                                                \n")
                 .append("     ,   ?                                                                                                                \n")
                 .append("     ,   ?                                                                                                                \n")
                 .append("     ,   TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')                                                                             \n")
                 .append(" )                                                                                                                        \n");
            
            System.out.println("111111111111111111111");    

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            System.out.println("111111111111111111112");    

            pstmt.setString(++iIdxAdd, v_userid     );
            pstmt.setString(++iIdxAdd, v_code       );
            pstmt.setInt   (++iIdxAdd, v_mileagebase);
            pstmt.setString(++iIdxAdd, v_userid     );
            
            System.out.println("111111111111111111113");    

            isOk2 = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
            
            sbSQL.setLength(0);
            
            sbSQL.append(" UPDATE Tz_Member         SET                 \n")
                 .append("    MileagePoint = MileagePoint + ?           \n")
                 .append(" WHERE  UserId    = ?                         \n");
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            System.out.println("111111111111111111114");    
            
            iIdxAdd = 0;
            
            pstmt.setInt   (++iIdxAdd, v_mileagebase);
            pstmt.setString(++iIdxAdd, v_userid     );
            
            System.out.println("111111111111111111115");    

            isOk2 = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            System.out.println(" isOk2 : " + isOk2);
            
            if ( isOk2 > 0 )
                connMgr.commit();
            else
                connMgr.rollback();
        } catch ( SQLException e ) {
            isOk2   = 0;
            
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
                
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk2   = 0;
            
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();                   } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(false);    } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();        } catch ( Exception e ) { } }
        }
     
        return isOk2;
    }
    

    /**
     * 마일리지 저장
     * @param  code        마일리지 코드
     * @param  userid      USER ID
     * @return result      0 : insert or update success  1 : insert or update fail
     * @throws Exception
     */
    public static int insertMileage(DBConnectionManager connMgr, String v_code, String v_userid) throws Exception {
        PreparedStatement   pstmt           = null;

        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        int                 iIdxAdd         = 0;
        
        int                 cnt             = 0;
        int                 isOk1           = MileageManager.getMileageUse(v_code);
        int                 isOk2           = 0;
        int                 v_mileagebase   = 0;
        
        if ( isOk1 == 0 )
            return isOk1;
        
        v_mileagebase                       = MileageManager.getMileageBase(v_code);

/*        
        if ( v_mileagebase == 0 )
            v_mileagebase                   = -1 * MileageManager.getMemberMileagePoint(v_userid);
*/            
        try {
            sbSQL.append(" INSERT INTO Tz_Member_Mileage                                                                                            \n")
                 .append(" (                                                                                                                        \n")
                 .append("         UserId                                                                                                           \n")
                 .append("     ,   MileageSeq                                                                                                       \n")
                 .append("     ,   MileageCode                                                                                                      \n")
                 .append("     ,   Point                                                                                                            \n")
                 .append("     ,   LUserId                                                                                                          \n")
                 .append("     ,   LDate                                                                                                            \n")
                 .append(" ) VALUES (                                                                                                               \n")
                 .append("         ?                                                                                                                \n")
                 .append("     ,   (SELECT NVL(MAX(MileageSeq) + 1, 1) FROM Tz_Member_Mileage WHERE UserId = " + SQLString.Format(v_userid) + ")    \n")
                 .append("     ,   ?                                                                                                                \n")
                 .append("     ,   ?                                                                                                                \n")
                 .append("     ,   ?                                                                                                                \n")
                 .append("     ,   TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')                                                                             \n")
                 .append(" )                                                                                                                        \n");
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(++iIdxAdd, v_userid     );
            pstmt.setString(++iIdxAdd, v_code       );
            pstmt.setInt   (++iIdxAdd, v_mileagebase);
            pstmt.setString(++iIdxAdd, v_userid     );
            
            isOk2 = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
            
            sbSQL.setLength(0);
            
            sbSQL.append(" UPDATE Tz_Member         SET                 \n")
                 .append("    MileagePoint = CASE WHEN ( MileagePoint + ? ) < 0 THEN 0 ELSE MileagePoint + ? END    \n")
                 .append(" WHERE  UserId    = ?                         \n");
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            iIdxAdd = 0;
            
            pstmt.setInt   (++iIdxAdd, v_mileagebase);
            pstmt.setInt   (++iIdxAdd, v_mileagebase);
            pstmt.setString(++iIdxAdd, v_userid     );
            
            isOk2 = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
            
        } catch ( SQLException e ) {
            isOk2   = 0;
            System.out.println("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk2   = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();                   } catch ( Exception e ) { } }
        }
     
        return isOk2;
    }
    
   
    /**
     * 마일리지 저장
     * @param  code        마일리지 코드
     * @param  userid      USER ID
     * @return result      0 : insert or update success  1 : insert or update fail
     * @throws Exception
     */
    public static int deleteMileage(String v_userid) throws Exception {
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;

        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        int                 iIdxAdd         = 0;
        
        int                 cnt             = 0;
        int                 isOk2           = 0;
        int                 v_mileagebase   = 0;
        
        v_mileagebase                       = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" DELETE FROM Tz_Member_Mileage WHERE UserId = " + SQLString.Format(v_userid) + "  \n");
            
            System.out.println("111111111111111111111" + sbSQL.toString());    

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            System.out.println("111111111111111111112");    

//            pstmt.setString(++iIdxAdd, v_userid     );
            
            System.out.println("111111111111111111113");    

            isOk2 = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
            
            sbSQL.setLength(0);
            
            sbSQL.append(" UPDATE Tz_Member SET                         \n")
                 .append("    mileagePoint = 0                          \n")
                 .append(" WHERE  UserId    = ?                         \n");
            
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            System.out.println("111111111111111111114");    
            
            iIdxAdd = 0;
            
            pstmt.setString(++iIdxAdd, v_userid     );
            
            System.out.println("111111111111111111115");    

            isOk2 = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
            
            connMgr.commit();
        } catch ( SQLException e ) {
            isOk2   = 0;
            
            System.out.println("111111111111111111115-e");    
            
            
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
                
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            isOk2   = 0;
            
            System.out.println("111111111111111111115-e1");    
            
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception ex ) { } }
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();                   } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(false);    } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();        } catch ( Exception e ) { } }
        }
     
        return isOk2;
    }
}
