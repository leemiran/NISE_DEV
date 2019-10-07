// **********************************************************
//  1. ��      ��: Correction ADMIN BEAN
//  2. ���α׷���: CorrectionAdminBean.java
//  3. ��      ��: ÷����� ������ bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: 
//  7. ��      ��:
// **********************************************************

package com.ziaan.tutor;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;


public class CorrectionAdminBean { 
    private ConfigSet 	config	;
    private int 		row		;

    public CorrectionAdminBean() { 
        try { 
            config 	= new ConfigSet();
            row 	= Integer.parseInt(config.getProperty("page.manage.row") );	// �� ����� �������� row ���� �����Ѵ�
            // row = 100;
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    
    /**
    �λ縻 ���
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectList(RequestBox box) throws Exception { 
       DBConnectionManager 	connMgr 	= null;
       ListSet 				ls1     	= null;
       ArrayList 			list1   	= null;
       String 				sql1    	= "";
       DataBox 				dbox1  		= null;
 
	   String 				v_user_id   = box.getSession("userid");			// �α��� ���̵�
	   String 				v_subj	  	= box.getStringDefault("s_subjcourse", box.getString("p_subj"));	// 

       sql1  ="select seq, subj, userid, comments, luserid, ldate";
       sql1 +=" from TZ_Correction";
	   sql1 +=" where userid = '" +v_user_id + "'";
	   
	   if ( !v_subj.equals("ALL") )
		   sql1 +=" and subj = '" +v_subj + "'";
	   
	   sql1 +=" order by ldate desc";

	   try { 
		   connMgr 	= new DBConnectionManager();
           list1 	= new ArrayList();

           ls1 		= connMgr.executeQuery(sql1);
                             
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
    �λ縻 ���
    @param box      receive from the form object and session
    @return int		isOk
    */
    public int insert(RequestBox box) throws Exception { 
       DBConnectionManager 	connMgr		= null;
       PreparedStatement 	pstmt		= null;
       String 				sql1		= "";
       int 					isOk		= 0;
       
       String 				v_user_id	= box.getSession("userid");
       String 				v_subj		= box.getString("s_subjcourse");           // ����
       String 				v_comments	= box.getString("p_comment");          // ÷�賻��

       try { 
           connMgr 	= new DBConnectionManager();
           connMgr.setAutoCommit(false);
		   
           sql1  = "insert into TZ_CORRECTION(seq,subj,userid,comments,luserid,ldate) ";
		   sql1 += "values(CORRECTION_SEQ.nextval,?,?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS'))";	   
		   
           pstmt 	= connMgr.prepareStatement(sql1);
           
           pstmt.setString(1,v_subj		);
           pstmt.setString(2,v_user_id	);
           pstmt.setString(3,v_comments	);
           pstmt.setString(4,v_user_id	);

		   isOk 	= pstmt.executeUpdate();		   
		   
		   	if ( isOk > 0 ) { 
		   		connMgr.commit();
		  	} else { 
		  		connMgr.rollback();
		  	}
       } catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex, box, sql1);
           throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
       } finally { 
           if ( pstmt != null )   { try { pstmt.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }
       
       return isOk;
   }

   
    /**
    �λ縻 ����
    @param box      receive from the form object and session
    @return int		isOk
    */   
    public int update(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr		= null;
		PreparedStatement 	pstmt     	= null;
		String 				sql1        = "";
		int 				isOk        = 0;
		int 				v_seq		= box.getInt("p_seq");	
		
		// �Ϸù�ȣ
		String 				v_user_id	= box.getSession("userid");				// �α��� ���̵�
		String 				v_comments	= box.getString("p_comments" + v_seq);  // ÷�賻��

		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			sql1  = "update TZ_CORRECTION set comments=?,luserid=?,ldate=to_char(sysdate, 'YYYYMMDDHH24MISS') ";
			sql1 += "where seq=?";

			pstmt 	= connMgr.prepareStatement(sql1);

			pstmt.setString	(1, v_comments	);
			pstmt.setString	(2, v_user_id	);
			pstmt.setInt	(3, v_seq		);

			isOk = pstmt.executeUpdate();
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		} finally { 
			if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return isOk;
    }


	/**
	�λ縻 ����
	@param box      receive from the form object and session
	@return int		isOk
	*/
    public int delete(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr		= null;
		PreparedStatement 	pstmt   	= null;
		String 				sql1    	= "";
		int 				isOk        = 0;
		int 				v_seq       = box.getInt("p_seq");

		try { 
			connMgr	= new DBConnectionManager();
			 
			sql1 	= "delete TZ_CORRECTION where seq=?";
			pstmt 	= connMgr.prepareStatement(sql1);

			pstmt.setInt(1,v_seq);

			isOk = pstmt.executeUpdate();
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		} finally { 
			if ( pstmt != null )   { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
    }
}