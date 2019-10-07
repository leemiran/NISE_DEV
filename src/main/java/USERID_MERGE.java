import java.sql.*;
import cubrid.jdbc.driver.*;
import cubrid.sql.*;


public class USERID_MERGE {
	
	
	
	public static void USERID_MERGE(String tobe_userid, String asis_userid) throws Exception {
		
		
		Connection  conn = null;
		PreparedStatement   pstmt   = null;
		ResultSet rs = null;        
		int isOk = 0;
		
		try {			
			//Class.forName("com.tmax.tibero.jdbc.TbDriver");			
			//conn = DriverManager.getConnection("jdbc:tibero:thin:@112.217.104.35:8629:tibero", "newknise", "newknise");
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection("jdbc:default:connection");
            //conn = DriverManager.getConnection("jdbc:cubrid:203.236.236.27:30000:newknise:::", "nise", "nise");
		}catch(Exception e){
			e.printStackTrace();
			//throw new Exception( e.getMessage() );
		}	
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" merge INTO tz_progress a ");
		sql.append(" USING ( ");
		sql.append(" 	SELECT b.[year], b.subj, b.subjseq, b.userid, c.lesson, ? AS ch_id ");
		sql.append(" 		FROM tz_student b ");
		sql.append(" 		    ,tz_subjlesson c ");
		sql.append(" 		WHERE userid = ? ");
		sql.append(" 		AND b.subj = c.subj ");
		sql.append(" )b ");
		sql.append(" ON ( ");
		sql.append(" 	a.subj = b.subj ");
		sql.append(" 	AND a.[year] = b.[year] ");
		sql.append(" 	AND a.subjseq = b.subjseq ");
		sql.append(" 	AND a.lesson = b.lesson ");
		sql.append(" 	AND a.userid = b.userid	 ");
		sql.append(" ) ");
		sql.append(" when matched then  ");
		sql.append(" update set a.userid = b.ch_id ");
		
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
							
		isOk = pstmt.executeUpdate();
			
		
			
		sql = new StringBuffer();
		sql.append(" update tz_propose  ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ? ");
		sql.append(" ; ");
System.out.println("sql "+sql);
			
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
							
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);
		
			
		sql = new StringBuffer();
		sql.append(" update PA_PAYMENT a ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ? ");
		sql.append(" ; ");
System.out.println("sql "+sql);
			
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
							
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);
			
		sql = new StringBuffer();
		sql.append(" update TZ_STUDENT a ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ? ");
		sql.append(" ; ");
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
		
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);	
			
		sql = new StringBuffer();	 
		sql.append(" update tu_projrep a ");
		sql.append(" set projid = ? ");
		sql.append(" where projid = ? ");
		sql.append(" ; ");
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
		
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);	

		sql = new StringBuffer();
		sql.append(" update TZ_EXAMRESULT a ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ? ");
		sql.append(" ; ");
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
		
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);	

		sql = new StringBuffer();
		sql.append(" update TZ_EXAMRESULTTEMP a ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ?");
		sql.append(" ; ");
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
		
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);				
			
		sql = new StringBuffer();
		sql.append(" update TZ_SULEACH a ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ? ");
		sql.append(" ; ");
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
		
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);				

		sql = new StringBuffer();
		sql.append(" update TZ_STOLD a ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ? ");
		sql.append(" ; ");
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
		
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);				
			
		sql = new StringBuffer();  
		sql.append(" update tz_attendance a ");
		sql.append(" set userid = ? ");
		sql.append(" where userid = ?");
		sql.append(" ; ");
System.out.println("sql "+sql);
		
		pstmt = conn.prepareStatement(sql.toString());			
		pstmt.setString(1, tobe_userid);
		pstmt.setString(2, asis_userid);
		
		isOk = pstmt.executeUpdate();
System.out.println("isOk  "+isOk);				


sql = new StringBuffer();  
			sql.append(" delete ");
			sql.append(" from tz_member a ");
			sql.append(" where userid = ? ");
			sql.append(" ; ");
			System.out.println("sql "+sql);
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, asis_userid);
			
			isOk = pstmt.executeUpdate();
	System.out.println("isOk  "+isOk);				

			
	        pstmt.close();

		//return isOk;
	}


	/*public static void main(String args[]) throws Exception{
		USERID_MERGE("kyh2328164", "k02semi2") ;
	}*/
}


