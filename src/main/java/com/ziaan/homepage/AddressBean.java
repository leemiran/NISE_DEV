/**
 * 주소록 관리
 * @author LEE SEUNG LEE
 * @version 1.0
 */

package com.ziaan.homepage;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

//import org.apache.commons.lang.StringUtils;

import com.ziaan.common.UpdateSpareTable;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.DatabaseExecute;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.PreparedBox;
import com.ziaan.library.RequestBox;

public class AddressBean {
    private ConfigSet config;
    private int row;
    private String dirUploadDefault;
    	
    public AddressBean() {
        try{
            config = new ConfigSet();  
            row = Integer.parseInt(config.getProperty("page.bulletin.row"));        //        이 모듈의 페이지당 row 수를 셋팅한다
            dirUploadDefault = config.getProperty("dir.upload.default");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** 
    주소 목록을 가져온다.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListAddress(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try {
			
        	String s_userid = box.getSession("userid");
        	
            db = new DatabaseExecute();

            sql = "\n\r SELECT A.USERID userid, A.ADDRESSID addressid, A.TITLE title, ";
            sql += "\n\r (SELECT COUNT(buddy) FROM TU_ADDRESSDETAIL ";
            sql += "\n\r WHERE A.USERID = userid AND A.ADDRESSID = addressid) cnt ";
            sql += " FROM  TU_ADDRESS A ";
            sql += " WHERE USERID = '"+s_userid+"'";
            sql += "\n\r ORDER BY addressid DESC";            
                        
            box.put("p_isPage", new Boolean(true));     //    리스트 검색시 페이지 나누기가 있는 경우
            box.put("p_row", new Integer(row));     //    리스트 검색시 페이지 나누기가 있는 게시판 경우
            
            list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    /** 
    주소록 명단을 가져온다.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListBuddy(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try {
			
        	String s_userid = box.getSession("userid");
        	int	   v_addressid = box.getInt("p_addressid");
        	
            db = new DatabaseExecute();
            
            sql =  "\n\r SELECT A.ADDRESSID addressid, get_position(B.MTYPE) mtype, A.BUDDY buddy, ";
            sql += "\n\r get_dangwa(C.COMPNM,'') dangwa, get_hakgwa(C.COMPNM, '') hakgwa, A.BUDDYEMAIL buddyemail, ";
            sql += "\n\r a.BUDDYMOBILE mobile, B.NAME name ";
            sql += "\n\r FROM TU_ADDRESSDETAIL A, TU_MEMBER B, TU_COMP C ";
            sql += "\n\r WHERE A.ADDRESSID = "+ v_addressid;
            sql += "\n\r AND	  A.USERID    = '"+s_userid+"'";
            sql += "\n\r AND   A.BUDDY = B.USERID ";
            sql += "\n\r AND	  B.COMP = C.COMP(+)";
                        
            list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }
    
    /** 
    주소록 내용을 조회한다.
    @param RequestBox box 
    @return ArrayList
    */
    public DataBox selectViewBuddy(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;        
        String sql = "";

        try {

        	String s_userid = box.getSession("userid");
        	int	   v_addressid = box.getInt("p_addressid");
        	
            db = new DatabaseExecute();

            sql = "\n\r SELECT A.USERID userid, A.ADDRESSID addressid, A.TITLE title, ";
            sql += "\n\r (SELECT COUNT(buddy) FROM TU_ADDRESSDETAIL ";
            sql += "\n\r WHERE A.USERID = userid AND A.ADDRESSID = addressid) cnt ";
            sql += "\n\r FROM  TU_ADDRESS A ";
            sql += "\n\r WHERE USERID = '"+s_userid+"'";
            sql += "\n\r AND   ADDRESSID ='"+v_addressid+"'";        	
        	
            dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;        
	}
    
    /** 
    메일 주소록 제목을 등록한다.
    @param RequestBox box 
    @return boolean
    */
     public boolean insertAdress(RequestBox box) throws Exception {  
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        
        boolean isCommit = false;
        DatabaseExecute db = null;
                    
        try {
        	box.put("notTransaction", new Boolean(true));
            db = new DatabaseExecute(box);

            String s_userid = box.getSession("userid");
            
            //----------------------   일련 번호 가져온다 ----------------------------
            sql = " SELECT NVL(MAX(addressid), 0)+1 addressid FROM TU_ADDRESS";              
  
            dbox = db.executeQuery(box, sql);
            int v_addressid = dbox.getInt("d_addressid");
            //-----------------------------------------------------------------------------------              
            System.out.println("어드레스 아이디 ::::::"+v_addressid);
            String v_title = box.getString("p_title");
            
            //////////////////////////////////   게시판 table 에 입력  ///////////////////////////////////////////////////////////////////
            sql1 = "INSERT INTO TU_ADDRESS (userid, addressid, title, luserid, ldate)" +
            " VALUES( ?, ?, ?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'))";     
            
            PreparedBox pbox1 = new PreparedBox("preparedbox");
            
            pbox1.setString(1, s_userid);
            pbox1.setInt(2, v_addressid);              
            pbox1.setString(3, v_title);              
            pbox1.setString(4, s_userid);
            
            /*PreparedBox pbox2 = new PreparedBox("preparedbox");

            sql2 = UpdateSpareTable.setSpareSql();
            UpdateSpareTable.setSparePbox(pbox2);*/
            
            isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});

        }
        catch (Exception ex) {             
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }
     
     /** 
     인명조회를 한다.
     @param RequestBox box 
     @return boolean
     */
      public ArrayList listAddress(RequestBox box) throws Exception {  
    	  
          ArrayList list = null;
          String sql = "";
          DatabaseExecute db = null;
          
          try {
        	db = new DatabaseExecute();
          	
        	String v_search = box.getString("p_searchstr");
          	String v_gubun  = box.getString("p_select");
          	
          	System.out.println(v_search);
          	System.out.println(v_gubun);
              
            sql =  "\n\r SELECT get_position(mtype) mtype, ";
            sql += "\n\r get_dangwa(comp,'') dangwa, get_hakgwa(comp, '') hakgwa,name, userid, email, ";
            sql += "\n\r mobile ";
            sql += "\n\r FROM TU_MEMBER ";
            sql += "\n\r WHERE usecode NOT IN ('03','04','9','08')";
              
            if(v_gubun.equals("2")){
            	sql += "\n\r AND name LIKE '%"+v_search+"%'";
            }else if(v_gubun.equals("1")){
            	sql += "\n\r AND userid LIKE '%"+v_search+"%'";
            }
            //sql += "\n\r and rownum = 1";
            
            box.put("p_isPage", new Boolean(true));     //    리스트 검색시 페이지 나누기가 있는 경우
            box.put("p_row", new Integer(row));     //    리스트 검색시 페이지 나누기가 있는 게시판 경우
                          
            list = db.executeQueryList(box, sql);
          }
          catch (Exception ex) {
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
          }
          return list;
      }
      
    /** 
    인명조회결과를 등록 한다.
    @param RequestBox box 
    @return boolean
    */
    public boolean addAddress(RequestBox box) throws Exception {    	
    	DataBox dbox = null;
    	ArrayList list = null;
        String sql1 = "";        
        String sql2 = "";
        String sql3 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;
        
        String s_userid    = box.getSession("userid");
        int	   v_addressid = box.getInt("p_addressid");
        Vector v_checks    = box.getVector("p_checks");
        Vector v_email     = box.getVector("p_email");
        
        
        Vector v_buddy     = new Vector();
        Vector v_mail	   = new Vector();
        Vector v_mobile	   = new Vector();
        
        Enumeration ev1 = v_checks.elements();
        Enumeration ev2 = v_email.elements();
        Enumeration ev3 = v_mobile.elements();
        
        try {
          	 
          	db = new DatabaseExecute(box);
          	
          	while(ev1.hasMoreElements()){

               	String temp1 = (String)ev1.nextElement();
               	String temp2 = (String)ev2.nextElement();                
               		
               	sql1 =  "\n\r SELECT COUNT(*) cnt FROM TU_ADDRESSDETAIL";
                sql1 += "\n\r WHERE BUDDY = '"+temp1+"'";
                sql1 += "\n\r AND	ADDRESSID ="+v_addressid;
                sql1 += "\n\r AND	USERID ='"+s_userid+"'";

                dbox = db.executeQuery(sql1);
                    
                if(dbox.getInt("d_cnt") == 0){
                	
                	sql2 = " select userid, email, mobile from tu_member ";
                	sql2 += " where userid ='"+temp1+"'";
                	
                	dbox = db.executeQuery(sql2);
                			
                	//list = db.executeQueryList(sql2);
                	//v_buddy.add(temp1);
                	//v_mail.add(dbox.getString("d_email"));
                	v_buddy.add(temp1);
                	v_mail.add(dbox.getString("d_email"));
                	v_mobile.add(dbox.getString("d_mobile"));
                }
            }
             
          	for(int i=0; i < v_buddy.size(); i++) {
                System.out.println(v_mail.get(i));
                System.out.println(v_buddy.get(i));
          	}          	
            
            sql3 = "INSERT INTO TU_ADDRESSDETAIL(userid, addressid, buddy, buddyemail, buddymobile, ldate)" +
            " VALUES( ?, ?, ?, ?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'))";     
            
            PreparedBox pbox2 = new PreparedBox("preparedbox");
            
            pbox2.setString(1, s_userid);
            pbox2.setInt(2, v_addressid);            
            pbox2.setVector(3, v_buddy);            
            pbox2.setVector(4, v_mail);
            pbox2.setVector(5, v_mobile);
                        
            isCommit = db.executeUpdate(new PreparedBox [] {pbox2},  new String [] {sql3});
        }catch (Exception ex) {             
          ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
          throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
      }
        return isCommit;
    }
     
    /** 
    주소를 삭제 한다.
    @param RequestBox box 
    @return boolean
    */
    public boolean deleteAddressAll(RequestBox box) throws Exception {  
    	 //DataBox dbox = null;
         
         String sql2 = "";
         String sql3 = "";
         
         
         boolean isCommit = false;
         DatabaseExecute db = null;
                     
         try {
        	 box.put("notTransaction", new Boolean(true));  
        	 
        	 db = new DatabaseExecute(box);             

             String s_userid = box.getSession("userid");
             Vector	v_checks = box.getVector("p_checks");
             
             sql2 =  " DELETE FROM TU_ADDRESSDETAIL ";
          	 sql2 += " WHERE userid = ?";
          	 sql2 += " AND	addressid = ?";
          	 
          	 PreparedBox pbox2 = new PreparedBox("preparedbox");

             pbox2.setString(1, s_userid);
             pbox2.setVector(2, v_checks);
            
             sql3 =  " DELETE FROM TU_ADDRESS ";
             sql3 += " WHERE userid = ? " ;
             sql3 += " AND	addressid = ?";
            
             PreparedBox pbox3 = new PreparedBox("preparedbox");

             pbox3.setString(1, s_userid);
             pbox3.setVector(2, v_checks);
             
             isCommit = db.executeUpdate(new PreparedBox [] {pbox2, pbox3},  new String [] {sql2, sql3});
         }
         catch (Exception ex) {             
             ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
             throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
         }
         return isCommit;
     }
     
     /** 
     주소록중 친구만 삭제 한다.
     @param RequestBox box 
     @return boolean
     */
     public boolean deleteAdressBuddy(RequestBox box) throws Exception {  
    	 
         String sql1 = "";
         
         boolean isCommit = false;
         DatabaseExecute db = null;
                     
         try {
        	 box.put("notTransaction", new Boolean(true));  
        	 
        	 db = new DatabaseExecute(box);             

        	 int    v_addressid = box.getInt("p_addressid");
             Vector	v_checks = box.getVector("p_checks");
             
             sql1 =  " DELETE FROM TU_ADDRESSDETAIL ";
          	 sql1 += " WHERE addressid = ?";
          	 sql1 += " AND	 buddy = ?";
          	 
          	 PreparedBox pbox1 = new PreparedBox("preparedbox");

             pbox1.setInt(1, v_addressid);
             pbox1.setVector(2, v_checks);
             
             isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});
         }
         catch (Exception ex) {             
             ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
             throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
         }
         return isCommit;
     }

}
