// **********************************************************
//  1. ��      ��: 
//  2. ���α׷���: OverLapBean.java
//  3. ��      ��:
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 0.1
//  6. ��      ��: ��â�� 2005. 7. 16
//  7. ��      ��:
// **********************************************************
package com.ziaan.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;

public class OverLapBean { 
	
	public OverLapBean() { }
	
	
    /**
    Member ���� ���� üũ
    @param userid           USER ID
    @return String Return
    @ ��û�����ϸ� true (�ߺ��ȵ�)
    @ ��û�Ұ����ϸ� false(�ߺ���)
    */
     public String isExitMember(String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";
        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitMember(connMgr, userid);
        }
        catch ( Exception ex ) { 
        }

        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }



    /**
    Member ���� ���� üũ
    @param userid           USER ID
    @return String Return
    @ ��û�����ϸ� true (�ߺ��ȵ�)
    @ ��û�Ұ����ϸ� false(�ߺ���)
    */
     public String isExitMember(DBConnectionManager connMgr, String userid) throws Exception { 
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        String sql         = "";
        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            sql +="select count(*) CNT from tz_member where userid = '" +userid + "' \n";
            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";   // ����
                }
                else{ 
                  errvalue = "1";   //  �λ�DB�� �������� �ʽ��ϴ�.
                }
            }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
        }
    return errvalue;
   }
   
   
   
   
   /**
    Member ���� ���� üũ
    @param box      receive from the form object and session
    @return String Return
    @ ��û�����ϸ� true (�ߺ��ȵ�)
    @ ��û�Ұ����ϸ� false(�ߺ���)
    */
     public String isExitMember(String userid, String isretired, String isemtpty, String isstoped) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitMember(connMgr , userid, isretired, isemtpty, isstoped);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }
    
    
    
    /**
    Member ���� ���� üũ
    @param box      receive from the form object and session
    @return String Return
    @ ��û�����ϸ� true (�ߺ��ȵ�)
    @ ��û�Ұ����ϸ� false(�ߺ���)
    */
     public String isExitMember(DBConnectionManager connMgr, String userid, String isretired, String isemtpty, String isstoped) throws Exception { 
        DataBox dbox= null;
        ListSet ls1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            sql +="select office_gbn from tz_member where userid = '" +userid + "' \n";
            System.out.println("sql_office_gbn == >>  >> " +sql);
            
            ls1 = connMgr.executeQuery(sql);

            if ( ls1.next() ) { 
                if ( ls1.getString("office_gbn").equals("N") && isretired.equals("Y") ) { 
                    errvalue = "2";   // �������Դϴ�.
                }
                else{ 
                  errvalue = "0";   // ����
                }

            } else { 
              errvalue = "1";  // �λ�DB�� �������� �ʽ��ϴ�.
            }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }

        return errvalue;
    }
    
    
    
     /**
     ������ �������� ���εǾ��ִ��� ����
     @param box      receive from the form object and session
     @return String Return
     @ "S1" ���εǾ� ����
     @ "0"  ���εǾ� ���� ����
     */
     public String isExitSulPaper(String sulgubun, int sulnum) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitSulPaper(connMgr , sulgubun, sulnum);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }
    
     /**
     ������ �������� ���εǾ��ִ��� ����
     @param box      receive from the form object and session
     @return String Return
     @ "S1" ���εǾ� ����
     @ "0"  ���εǾ� ���� ����
     */
     public String isExitSulPaper(DBConnectionManager connMgr, String sulgubun, int sulnum) throws Exception { 
        DataBox dbox= null;
        PreparedStatement   pstmt   = null;
        ResultSet rs = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            
            sql = " select                               \n";
            sql += "   count(*) cnt                       \n";
            sql += " from                                 \n";
            sql += "   tz_sulpaper                        \n";
            sql += " where                                \n";
            // sql += "   subj = '" +subj + "'                          \n";
            // sql += "   and grcode = '" +grcode + "'                    \n";
            // sql += "   and year = '" +year + "'                      \n";
            // sql += "   and subjseq = '" +subjseq + "'                   \n";
            sql += "   subj = '" +sulgubun + "'    \n";
            
            sql += "   and ','||sulnums||',' like '%," +sulnum + ",%' \n";
            
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if ( rs.next() ) { 
            	cnt = rs.getInt("cnt");
            	if ( cnt > 0) { 
            	  errvalue = "S1";
            	}
            	else{ 
            	  errvalue = "0";
            	}
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs   != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }
        return errvalue;
    }
    
    
    /**
     ���� ������ �����ڰ� �ִ��� ����
     @param box      receive from the form object and session
     @return String Return
     @ "S2" ���εǾ� ����
     @ "0"  ���εǾ� ���� ����
     */
     public String isExitSulEach(String sulgubun, int sulnum) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitSulEach(connMgr , sulgubun, sulnum);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }
    
     /**
     ������ �������� ���εǾ��ִ��� ����
     @param box      receive from the form object and session
     @return String Return
     @ "S2" ���εǾ� ����
     @ "0"  ���εǾ� ���� ����
     */
     public String isExitSulEach(DBConnectionManager connMgr, String sulgubun, int sulnum) throws Exception { 
        DataBox dbox= null;
        PreparedStatement   pstmt   = null;
        ResultSet rs = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
        	
        	sql += " select                                                          \n";
            sql += "   count(*) cnt                                                  \n";
            sql += " from                                                            \n";
            sql += "   tz_suleach                                                    \n";
            sql += " where                                                           \n";
            sql += "   1=1                                                           \n";
            
            if ( sulgubun.equals("ALL") ) { 
              sql += "   AND grcode != 'ALL' and ( subj != 'COMMON' and subj != 'TARGET' and subj != 'CP' ) \n";
            } else if ( sulgubun.equals("COMMON") ) { 
              sql += "   AND subj = 'COMMON'\n";
            } else if ( sulgubun.equals("TARGET") ) { 
              sql += "   AND subj = 'TARGET'\n";
            } else if ( sulgubun.equals("CONTENTS") ) { 
              sql += "   AND grcode = 'ALL' and ( subj != 'COMMON' and subj != 'TARGET' and subj != 'CP' ) \n";
            } else if ( sulgubun.equals("CP") ) { 
              sql += "   AND subj = 'CP'    \n";
            }
            sql += "   and ','||sulnums||',' like '%," +sulnum + ",%' \n";
            
Log.info.println(" >>  >>  >>  >>  >> " +sql);            
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if ( rs.next() ) { 
            	cnt = rs.getInt("cnt");
            	if ( cnt > 0) { 
            	  errvalue = "S2";
            	}
            	else{ 
            	  errvalue = "0";
            	}
            }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs   != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }
        return errvalue;
    }
    
    
    /**
     �������� �����ڰ� �ִ��� ����
     @param box      receive from the form object and session
     @return String Return
     @ "S3" ���εǾ� ����
     @ "0"  ���εǾ� ���� ����
     */
     public String isExitSulPaperApply(String sulgubun, int sulpapernum) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitSulPaperApply(connMgr , sulgubun, sulpapernum);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }
    
     /**
     �������� �����ڰ� �ִ��� ����
     @param box      receive from the form object and session
     @return String Return
     @ "S3" ���εǾ� ����
     @ "0"  ���εǾ� ���� ����
     */
     public String isExitSulPaperApply(DBConnectionManager connMgr, String sulgubun, int sulpapernum) throws Exception { 
        DataBox dbox= null;
        PreparedStatement   pstmt   = null;
        ResultSet rs = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
        	
        	sql += " select                                                          \n";
            sql += "   count(*) cnt                                                  \n";
            sql += " from                                                            \n";
            sql += "   tz_suleach                                                    \n";
            sql += " where                                                           \n";
            sql += "   1=1                                                           \n";
            
            if ( sulgubun.equals("ALL") ) { 
              sql += "   AND grcode != 'ALL' and ( subj != 'COMMON' and subj != 'TARGET' and subj != 'CP' ) \n";
            } else if ( sulgubun.equals("COMMON") ) { 
              sql += "   AND subj = 'COMMON'\n";
            } else if ( sulgubun.equals("TARGET") ) { 
              sql += "   AND subj = 'TARGET'\n";
            } else if ( sulgubun.equals("CONTENTS") ) { 
              sql += "   AND grcode = 'ALL' and ( subj != 'COMMON' and subj != 'TARGET' and subj != 'CP' )\n";
            } else if ( sulgubun.equals("CP") ) { 
              sql += "   AND subj = 'CP'    \n";
            }
            sql += "   and sulpapernum = " +sulpapernum + " \n";
            
            
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            
            if ( rs.next() ) { 
            	cnt = rs.getInt("cnt");
            	if ( cnt > 0) { 
            	  errvalue = "S3";
            	}
            	else{ 
            	  errvalue = "0";
            	}
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs   != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }
        return errvalue;
    }
    
}