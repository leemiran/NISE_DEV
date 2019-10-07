// **********************************************************
//  1. 제      목: 고사장 관리
//  2. 프로그램명 : ExaminationRoomBean.java
//  3. 개      요: 고사장 관리
//  4. 환      경: JDK 1.5
//  5. 버      젼: 고사장
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Collections;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
 
public class ExaminationRoomBean { 
	
	private ConfigSet config;
    private int row; 
    private int adminrow; 
    private	static final String	FILE_TYPE =	"p_file";			// 		파일업로드되는 tag name
	private	static final int FILE_LIMIT	= 5;					// 	  페이지에 세팅된 파일첨부 갯수
    
    
	public ExaminationRoomBean() { 
	    try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
            adminrow = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }	
		
	}

	/**
	* 교육청 리스트
	* @param box          receive from the form object and session
	* @return int        
	* @throws Exception
	*/
	public ArrayList selectArea(RequestBox box) throws Exception { 
		 DBConnectionManager	connMgr	= null;
	     ListSet             ls      = null;
	     ArrayList           list    = null;
	     String              sql     = "";
	     DataBox             dbox    = null;


		 try { 
			 connMgr = new DBConnectionManager();
			list = new ArrayList();

			 sql=" select orgid,org_nm                                                                              \n";
			 sql+=" ,decode(orgid,'M01','서울','M02','부산','M03','대구','M04','인천','M05','광주'                  \n";
			 sql+=" ,'M06','대전','M07','울산','M08','경기','M09','강원','M10','충북','M11','충남','M12','전북'     \n";
			 sql+=" ,'M13','전남','M14','경북','M15','경남','M16','제주') as areanm                                 \n";
			 sql+=" from TZ_EDUORG where parord='M'                                                                 \n";
			 sql+=" and orgid <>'M20'                                                                               \n";
			 
			 //System.out.println("sql=====>"+sql);
			 
			 ls = connMgr.executeQuery(sql);

			   while ( ls.next() ) { 
	                dbox = ls.getDataBox();
	                list.add(dbox);
	            }

		 }
		 catch ( Exception ex ) { 
			 ErrorManager.getErrorStackTrace(ex, box, sql);
			 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		 }
		 finally { 
			 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		 }
		 return list;
	 }
	
	
	/**
	* 교육청 리스트
	* @param box          receive from the form object and session
	* @return int        
	* @throws Exception
	*/
	public ArrayList selectArea2(RequestBox box) throws Exception { 
		 DBConnectionManager	connMgr	= null;
	     ListSet             ls      = null;
	     ArrayList           list    = null;
	     String              sql     = "";
	     DataBox             dbox    = null;

         String v_area = box.getString("p_area");
		 try { 
			 connMgr = new DBConnectionManager();
			 list = new ArrayList();

			 sql=" select orgid,org_nm from TZ_EDUORG where orgid like '"+v_area+"%'  and  orgid <>'"+v_area+"'      \n";
		
		
			 System.out.println("sql=====>"+sql);
			 
			 ls = connMgr.executeQuery(sql);

			   while ( ls.next() ) { 
	                dbox = ls.getDataBox();
	                list.add(dbox);
	            }

		 }
		 catch ( Exception ex ) { 
			 ErrorManager.getErrorStackTrace(ex, box, sql);
			 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		 }
		 finally { 
			 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		 }
		 return list;
	 }
	
	
	/**
	* 고사장 등록할때
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/
	public int insertData(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String sql  = "";
	
		int isOk  = 0;
		
        String p_area = box.getString("p_area");   //지역
        String p_gubun = box.getString("p_gubun");   //구분
        String p_edunm = box.getString("p_edunm"); //고사장명
        String p_post1 = box.getString("p_post1")+"-"+box.getString("p_post2"); //우편번호
        String p_addr = box.getString("p_addr")+box.getString("p_addr2"); //우편번호
        String p_shtel = box.getString("p_shtel"); //전호번호
        String p_homepage = box.getString("p_homepage"); //홈페이지주소
        String p_useyn = box.getString("p_useyn"); //사용유무
        
		String s_userid   = box.getSession("userid");
		int p_seq=0;
		try { 

		   connMgr = new DBConnectionManager();		   

		
           sql =  " select nvl(max(seq),0)+1 from TZ_ATTEND_CD  \n";
           ls =connMgr.executeQuery(sql);
           
           if(ls.next()){
             p_seq = ls.getInt(1);
           }
           ls.close();
           
           
           sql="insert into TZ_ATTEND_CD (seq,SCHOOL_NM,HIGH_EDUMIN,LOW_EDUMIN,SCHOOL_CLASS, \n";
           sql+=" FOUND_CLASS,PART_CLASS,JUSO,ZIP_CD,PHONE_NO,UPDATE_DT,UPDATE_ID,ISUSE ) \n";
           sql+="values(? ,? ,? ,? ,? ,? ,? ,? ,? ,? , to_char(sysdate, 'YYYYMMDDHH24MISS'),? ,? ) \n";
           
          // System.out.println("저장=====>"+sql);
                     
           pstmt = connMgr.prepareStatement(sql);
           
           pstmt.setInt   (1,  p_seq);
           pstmt.setString(2,  p_edunm);  //고사장명
           pstmt.setString(3,  p_homepage);  //홈페이지
           pstmt.setString(4,  p_area);       //지역
           pstmt.setString(5,  p_gubun);      //학교구분
           pstmt.setString(6,  "U");     //설립구분
           pstmt.setString(7,  "기타");     //계열구분
           pstmt.setString(8,  p_addr);     //주소
           pstmt.setString(9,  p_post1);     //우편번호
           pstmt.setString(10,  p_shtel);    //전화번호
           pstmt.setString(11,  s_userid);    //작성자
           pstmt.setString(12,  p_useyn);    //사용유무
         
           isOk = pstmt.executeUpdate();

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql - > " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	
	
	/**
	* 리스트
	* @param box          receive from the form object and session
	* @return int        
	* @throws Exception
	*/
	public ArrayList selectlist (RequestBox box) throws Exception { 
		 DBConnectionManager	connMgr	= null;
	     ListSet             ls      = null;
	     ArrayList           list    = null;
	     String              sql     = "";
	     DataBox             dbox    = null;

         String v_area = box.getString("p_area");
		 try { 
			 connMgr = new DBConnectionManager();
			 list = new ArrayList();

			 sql=" select seq,school_nm,phone_no,isuse,zip_cd,juso from TZ_ATTEND_CD  order by seq desc \n";
		
		 
			 ls = connMgr.executeQuery(sql);

			   while ( ls.next() ) { 
	                dbox = ls.getDataBox();
	                list.add(dbox);
	            }

		 }
		 catch ( Exception ex ) { 
			 ErrorManager.getErrorStackTrace(ex, box, sql);
			 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		 }
		 finally { 
			 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		 }
		 return list;
	 }
	
	
	/**
	* 리스트
	* @param box          receive from the form object and session
	* @return int        
	* @throws Exception
	*/
	public ArrayList selectView (RequestBox box) throws Exception { 
		 DBConnectionManager	connMgr	= null;
	     ListSet             ls      = null;
	     ArrayList           list    = null;
	     String              sql     = "";
	     DataBox             dbox    = null;

         String v_seq = box.getString("p_seq");
		 try { 
			 connMgr = new DBConnectionManager();
			 list = new ArrayList();

			 sql=" select seq,school_nm,high_edumin,low_edumin,school_class,found_class,part_class,juso,zip_cd,phone_no,update_dt,update_id,isuse  from TZ_ATTEND_CD  where seq ='"+v_seq+"'  \n";
		
		 
			 ls = connMgr.executeQuery(sql);

			   while ( ls.next() ) { 
	                dbox = ls.getDataBox();
	                list.add(dbox);
	            }

		 }
		 catch ( Exception ex ) { 
			 ErrorManager.getErrorStackTrace(ex, box, sql);
			 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		 }
		 finally { 
			 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		 }
		 return list;
	 }
	
	
	/**
	* 고사장 수정할때
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/
	public int updateData(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String sql  = "";
	
		int isOk  = 0;
		
        String p_area = box.getString("p_area");   //지역
        String p_gubun = box.getString("p_gubun");   //구분
        String p_edunm = box.getString("p_edunm"); //고사장명
        String p_post1 = box.getString("p_post1")+"-"+box.getString("p_post2"); //우편번호
        String p_addr = box.getString("p_addr");
        String p_shtel = box.getString("p_shtel"); //전호번호
        String p_homepage = box.getString("p_homepage"); //홈페이지주소
        String p_useyn = box.getString("p_useyn"); //사용유무
        
		String s_userid   = box.getSession("userid");
		String v_seq= box.getString("p_seq");
		try { 

		   connMgr = new DBConnectionManager();		   
           
           
           sql="update TZ_ATTEND_CD set SCHOOL_NM =? ,HIGH_EDUMIN= ? ,LOW_EDUMIN= ?, SCHOOL_CLASS =? , \n";
           sql+=" JUSO=? ,ZIP_CD=? ,PHONE_NO=? ,UPDATE_DT=to_char(sysdate, 'YYYYMMDDHH24MISS') ,UPDATE_ID=? ,ISUSE=?  \n";
           sql+=" where seq = ? \n";
           
                     
           pstmt = connMgr.prepareStatement(sql);
           
           
           pstmt.setString(1,  p_edunm);  //고사장명
           pstmt.setString(2,  p_homepage);  //홈페이지
           pstmt.setString(3,  p_area);       //지역
           pstmt.setString(4,  p_gubun);      //학교구분
           pstmt.setString(5,  p_addr);     //주소
           pstmt.setString(6,  p_post1);     //우편번호
           pstmt.setString(7,  p_shtel);    //전화번호
           pstmt.setString(8,  s_userid);    //작성자
           pstmt.setString(9,  p_useyn);    //사용유무
           pstmt.setString(10,  v_seq);
         
           isOk = pstmt.executeUpdate();

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql - > " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	

	/**
	* 고사장 삭제
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/
	public int deleteData(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String sql  = "";
	
		int isOk  = 0;
		
      
		String v_seq= box.getString("p_seq");
		try { 

		   connMgr = new DBConnectionManager();		   
           
           
           sql="delete from  TZ_ATTEND_CD where seq = ? \n";
           
                     
           pstmt = connMgr.prepareStatement(sql);
           
       
           pstmt.setString(1,  v_seq);
         
           isOk = pstmt.executeUpdate();

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql - > " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	
	/**
	* 기수개설 시 사용되는 리스트
	* @param box          receive from the form object and session
	* @return int        
	* @throws Exception
	*/
	public ArrayList selectExaminationRoomList(RequestBox box) throws Exception { 
		 DBConnectionManager	connMgr	= null;
	     ListSet             ls      = null;
	     ArrayList           list    = null;
	     String              sql     = "";
	     DataBox             dbox    = null;


		 try { 
			 connMgr = new DBConnectionManager();
			list = new ArrayList();

			 sql=" select seq,'['||low_edumin||']'||school_nm as snm from TZ_ATTEND_CD where isuse='Y' ";
			 
			 
			 ls = connMgr.executeQuery(sql);

			   while ( ls.next() ) { 
	                dbox = ls.getDataBox();
	                list.add(dbox);
	            }

		 }
		 catch ( Exception ex ) { 
			 ErrorManager.getErrorStackTrace(ex, box, sql);
			 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		 }
		 finally { 
			 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		 }
		 return list;
	 }


}
