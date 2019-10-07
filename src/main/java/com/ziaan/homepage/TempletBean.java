//**********************************************************
//1. ��      ��: ���� 
//2. ���α׷��� : TempletBean.java
//3. ��      ��: ����
//4. ȯ      ��: JDK 1.4
//5. ��      ��: 1.0
//6. ��      ��: ����ö
//7. ��     �� :  
//**********************************************************

package com.ziaan.homepage;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.common.UpdateSpareTable;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.DatabaseExecute;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.PreparedBox;
import com.ziaan.library.RequestBox;

public class TempletBean {
    private ConfigSet config;
    private int row;
    private String dirUploadDefault;
    	
    public TempletBean() {
        try{
            config = new ConfigSet();  
            row = Integer.parseInt(config.getProperty("page.bulletin.row"));  //�� ����� �������� row ���� �����Ѵ�
            dirUploadDefault = config.getProperty("dir.upload.default");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** 
    ���ø� ����� �����´�.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListTemplate(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";
        
        try {
			
            db = new DatabaseExecute();
            
            String v_search = box.getString("p_search");
            String v_userid = box.getSession("userid");

            sql = "\n\r SELECT userid, templateid, title ";			
            sql += "\n\r FROM TU_TEMPLATE";
            
            if(v_search != null){
            	sql += " WHERE title like '%"+ v_search +"%' ";
            }
            
            sql += "\n\r AND userid ='"+v_userid+"'";
            
            sql += "\n\r ORDER BY templateid DESC";   
                        
            box.put("p_isPage", new Boolean(true));     //    ����Ʈ �˻��� ������ �����Ⱑ �ִ� ���
            box.put("p_row", new Integer(row));     //    ����Ʈ �˻��� ������ �����Ⱑ �ִ� �Խ��� ���
            
            list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    /** 
    ���ø� ������ ��ȸ�Ѵ�.
    @param RequestBox box 
    @return DataBox
    */
    public DataBox selectViewTemplate(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;        
        String sql = "";

        try {

        	int v_templateid = box.getInt("p_templateid");
		    String v_userid = box.getString("p_userid");		    
		    
            db = new DatabaseExecute();
				    
	            sql = "\n\r SELECT userid, templateid, title, contents ";	            
	            sql += "\n\r FROM TU_TEMPLATE ";
	            sql += "\n\r WHERE templateid = "+ v_templateid;
	            sql += "\n\r AND userid = '"+v_userid+"'";
	            
	            dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;
        
	}

    /** 
    ���� ���ø��� ����Ѵ�.
    @param RequestBox box 
    @return boolean
    */
    public boolean insertTemplate(RequestBox box) throws Exception {  
    	DataBox dbox = null;
    	String sql = "";
    	String sql1 = "";
    	String sql2 = "";
    	boolean isCommit = false;
    	DatabaseExecute db = null;
                      
    	try {
    		db = new DatabaseExecute(box);

    		String s_userid = box.getSession("userid");
              
    		//----------------------   �Ϸ� ��ȣ �����´� ----------------------------
    		sql = " SELECT NVL(MAX(templateid), 0)+1 templateid FROM TU_TEMPLATE";              
    
    		dbox = db.executeQuery(box, sql);      
    		int v_templateid = dbox.getInt("d_templateid");
    		//-----------------------------------------------------------------------------------              
              
    		String v_title = box.getString("p_title");
    		String v_content = box.getString("p_contents");              
              
    		//////////////////////////////////   �Խ��� table �� �Է�  ///////////////////////////////////////////////////////////////////
    		sql1 = "INSERT INTO TU_TEMPLATE (userid, templateid, title, contents, luserid, ldate)" +
    		" VALUES( ?, ?, ?, EMPTY_CLOB(), ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'))";     
              
    		PreparedBox pbox1 = new PreparedBox("preparedbox");
              
    		pbox1.setString(1, s_userid);
    		pbox1.setInt(2, v_templateid);              
    		pbox1.setString(3, v_title);              
    		pbox1.setString(4, s_userid);             
              
    		//-----------���� �ش� content2 �� empty_clob()�� �����ϰ� ���� ���� ��Ʈ������ ġȯ�Ѵ�.                 
    		pbox1.setClob("select contents FROM TU_TEMPLATE WHERE templateid = " + v_templateid, v_content);        
              
                    
    		PreparedBox pbox2 = new PreparedBox("preparedbox");

    		sql2 = UpdateSpareTable.setSpareSql();
    		UpdateSpareTable.setSparePbox(pbox2);
              
    		isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql1, sql2});
 
    	}
    	catch (Exception ex) {             
    		ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
    		throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
    	}
    	return isCommit;
    }

    /** 
    ���ø� ������ �����Ѵ�.
    @param RequestBox box 
    @return boolean
    */
    public boolean updateTemplate(RequestBox box) throws Exception {              
    	String sql1 = "";
    	String sql2 = "";
        
    	boolean isCommit = false;        
    	DatabaseExecute db = null;
                     
    	try {
    		db = new DatabaseExecute(box);

            String v_userid	 = box.getString("p_userid");
            int	v_templateid = box.getInt("p_templateid");
            String v_title		 = box.getString("p_title");
            String v_contents   = box.getString("p_contents");
             
            /////   �Խ��� table �� �Է�  /////
            sql1 = "UPDATE TU_TEMPLATE SET title = ? " +
            " , contents =  EMPTY_CLOB() " +
            " WHERE userid = ? " +
            " AND	 templateid = ? ";
             
            PreparedBox pbox1 = new PreparedBox("preparedbox");
             
            pbox1.setString(1, v_title);
            pbox1.setString(2, v_userid);
            pbox1.setInt(3, v_templateid);
             
            //-----------���� �ش� content2 �� empty_clob()�� �����ϰ� ���� ���� ��Ʈ������ ġȯ�Ѵ�.                 
            pbox1.setClob("select contents FROM TU_TEMPLATE WHERE templateid = " + v_templateid + " AND userid = '" + v_userid + "'", v_contents);             
                   
            PreparedBox pbox2 = new PreparedBox("preparedbox");

            sql2 = UpdateSpareTable.setSpareSql();
            UpdateSpareTable.setSparePbox(pbox2);
             
            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql1, sql2});
    	}
        catch (Exception ex) {
        	ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
        	throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }

    /** 
    ���ø��� �����Ѵ�.
    @param RequestBox box 
    @return boolean
    */
    public boolean deleteTemplate(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        boolean isCommit = false;  
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        
        try {            
            db = new DatabaseExecute(box);
     
            String 	v_userid	 = box.getString("p_userid");
        	int		v_templateid = box.getInt("p_templateid");
        	Vector 	v_checks 	 = box.getVector("p_checks");
        	
            //////////////////////////////////   ���� ����  ///
            sql1 = "DELETE FROM TU_TEMPLATE WHERE userid = ? AND templateid = ? ";
            
            PreparedBox pbox1 = new PreparedBox("preparedbox");
            
            pbox1.setString(1, v_userid);
            if(v_checks.size() != 0){
            	pbox1.setVector(2, v_checks);
            }else{
            	pbox1.setInt(2, v_templateid);
            }
            
            PreparedBox pbox2 = new PreparedBox("preparedbox");
            
            sql2 = UpdateSpareTable.setSpareSql();
            UpdateSpareTable.setSparePbox(pbox2);

            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql1, sql2});
            
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }  

}
