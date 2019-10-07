// **********************************************************
//  1. ��      ��: ����� ����
//  2. ���α׷��� : ExaminationRoomBean.java
//  3. ��      ��: ����� ����
//  4. ȯ      ��: JDK 1.5
//  5. ��      ��: �����
//  6. ��      ��: 
//  7. ��      ��:
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
    private	static final String	FILE_TYPE =	"p_file";			// 		���Ͼ��ε�Ǵ� tag name
	private	static final int FILE_LIMIT	= 5;					// 	  �������� ���õ� ����÷�� ����
    
    
	public ExaminationRoomBean() { 
	    try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
            adminrow = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //        �� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }	
		
	}

	/**
	* ����û ����Ʈ
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
			 sql+=" ,decode(orgid,'M01','����','M02','�λ�','M03','�뱸','M04','��õ','M05','����'                  \n";
			 sql+=" ,'M06','����','M07','���','M08','���','M09','����','M10','���','M11','�泲','M12','����'     \n";
			 sql+=" ,'M13','����','M14','���','M15','�泲','M16','����') as areanm                                 \n";
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
	* ����û ����Ʈ
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
	* ����� ����Ҷ�
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
		
        String p_area = box.getString("p_area");   //����
        String p_gubun = box.getString("p_gubun");   //����
        String p_edunm = box.getString("p_edunm"); //������
        String p_post1 = box.getString("p_post1")+"-"+box.getString("p_post2"); //�����ȣ
        String p_addr = box.getString("p_addr")+box.getString("p_addr2"); //�����ȣ
        String p_shtel = box.getString("p_shtel"); //��ȣ��ȣ
        String p_homepage = box.getString("p_homepage"); //Ȩ�������ּ�
        String p_useyn = box.getString("p_useyn"); //�������
        
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
           
          // System.out.println("����=====>"+sql);
                     
           pstmt = connMgr.prepareStatement(sql);
           
           pstmt.setInt   (1,  p_seq);
           pstmt.setString(2,  p_edunm);  //������
           pstmt.setString(3,  p_homepage);  //Ȩ������
           pstmt.setString(4,  p_area);       //����
           pstmt.setString(5,  p_gubun);      //�б�����
           pstmt.setString(6,  "U");     //��������
           pstmt.setString(7,  "��Ÿ");     //�迭����
           pstmt.setString(8,  p_addr);     //�ּ�
           pstmt.setString(9,  p_post1);     //�����ȣ
           pstmt.setString(10,  p_shtel);    //��ȭ��ȣ
           pstmt.setString(11,  s_userid);    //�ۼ���
           pstmt.setString(12,  p_useyn);    //�������
         
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
	* ����Ʈ
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
	* ����Ʈ
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
	* ����� �����Ҷ�
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
		
        String p_area = box.getString("p_area");   //����
        String p_gubun = box.getString("p_gubun");   //����
        String p_edunm = box.getString("p_edunm"); //������
        String p_post1 = box.getString("p_post1")+"-"+box.getString("p_post2"); //�����ȣ
        String p_addr = box.getString("p_addr");
        String p_shtel = box.getString("p_shtel"); //��ȣ��ȣ
        String p_homepage = box.getString("p_homepage"); //Ȩ�������ּ�
        String p_useyn = box.getString("p_useyn"); //�������
        
		String s_userid   = box.getSession("userid");
		String v_seq= box.getString("p_seq");
		try { 

		   connMgr = new DBConnectionManager();		   
           
           
           sql="update TZ_ATTEND_CD set SCHOOL_NM =? ,HIGH_EDUMIN= ? ,LOW_EDUMIN= ?, SCHOOL_CLASS =? , \n";
           sql+=" JUSO=? ,ZIP_CD=? ,PHONE_NO=? ,UPDATE_DT=to_char(sysdate, 'YYYYMMDDHH24MISS') ,UPDATE_ID=? ,ISUSE=?  \n";
           sql+=" where seq = ? \n";
           
                     
           pstmt = connMgr.prepareStatement(sql);
           
           
           pstmt.setString(1,  p_edunm);  //������
           pstmt.setString(2,  p_homepage);  //Ȩ������
           pstmt.setString(3,  p_area);       //����
           pstmt.setString(4,  p_gubun);      //�б�����
           pstmt.setString(5,  p_addr);     //�ּ�
           pstmt.setString(6,  p_post1);     //�����ȣ
           pstmt.setString(7,  p_shtel);    //��ȭ��ȣ
           pstmt.setString(8,  s_userid);    //�ۼ���
           pstmt.setString(9,  p_useyn);    //�������
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
	* ����� ����
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
	* ������� �� ���Ǵ� ����Ʈ
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
