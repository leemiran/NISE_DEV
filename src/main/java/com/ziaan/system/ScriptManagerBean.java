//**********************************************************
//  1. 제      목: 
//  2. 프로그램명: ScriptManagerBean.java
//  3. 개      요: 
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0 QnA
//  6. 작      성: 이창훈 2006.1.31
//  7. 수      정:
//**********************************************************
package com.ziaan.system;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ScriptManagerBean {
        
    public ScriptManagerBean() {
        
    }


    /**
    * 
    * @param box          receive from the form object and session
    * @return int         
    * @throws Exception
    */
    public ArrayList selectListPage(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         ArrayList list	= null;
         DataBox dbox = null;
         list = new ArrayList();
         
         String sql = "";
         
         String v_prefix   = box.getStringDefault("p_prefix", "ALL");
         String v_language = box.getStringDefault("p_language", "KOREAN");
         String v_searchTxt   = box.getString("p_searchTxt");
         String v_searchselect= box.getString("p_searchselect");

         try {
             connMgr = new DBConnectionManager();

             sql = "select key, KOREAN, ENGLISH, JAPANESE, CHINESE from tz_languagescript ";
             sql+= " WHERE 1=1";
             if(!v_prefix.equals("ALL")){
               sql+= " and gubun = '"+v_prefix+"'";
             }
             if(!v_searchTxt.equals("")){
               if(v_searchselect.equals("key")){
                 sql+= " and key like '%"+v_searchTxt+"%'";
               }else if(v_searchselect.equals("value")){
                 sql+= " and "+v_language+" like '%"+v_searchTxt+"%'";
               }
             }
             sql+= " order by key";
             System.out.println(sql);
             ls = connMgr.executeQuery(sql);

             while( ls.next()) {
                //System.out.println("kkkkkkkkk");
                 dbox = ls.getDataBox();
                 list.add(dbox);
             }
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) {try {ls.close();} catch(Exception e){}}
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return list;
     }
     

    /**
    * 
    * @param box          receive from the form object and session
    * @return int         
    * @throws Exception
    */
    public DataBox selectListPageChoice(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         ArrayList list	= null;
         DataBox dbox = null;
         list = new ArrayList();
         
         String sql = "";

		String v_key   = box.getString("p_key");

         try {
             connMgr = new DBConnectionManager();

             sql = "select key, gubun, korean, english, japanese, chinese, comments, get_codenm('0020', gubun) gubunnm ";
             sql+= " from tz_languagescript where key = '"+v_key+"'";
             ls = connMgr.executeQuery(sql);
             
             if( ls.next()) {
                 dbox = ls.getDataBox();
             }

         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) {try {ls.close();} catch(Exception e){}}
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return dbox;
     }

 
    /**
    파일 을 디비로 저장 
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int SaveToDB(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";        
        int isOk = 1;
        String languagename = "";
        
        String val1 = "";        
        String val2 = "";        
        int cnt = 0;
    
        try {
           ArrayList list1 = selectListLang(box);
           
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           
             sql  =  "delete from  tz_LANGUAGESCRIPT ";
             pstmt1 = connMgr.prepareStatement(sql);
             isOk = pstmt1.executeUpdate();

             for(int i1=0; i1<list1.size(); i1++){
               DataBox dbox1 =(DataBox)list1.get(i1);
               languagename = dbox1.getString("d_languagename");
               PropertiesManager pt = PropertiesManager.getInstance(languagename);
               Enumeration e = pt.getEnumeration();
               
               while (e.hasMoreElements()) { 
                 String key = (String)e.nextElement(); 
                 String val = pt.getKeyValueEncode(key); 
                 
                 sql1 =  "select count(*) cnt from tz_LANGUAGESCRIPT where key = '"+key+"'";
                 ls = connMgr.executeQuery(sql1);
                 if(ls.next()){
                    cnt = ls.getInt("cnt");
                 }
                 ls.close();
                 
                 if(cnt < 1) {
                   sql1 =  "insert into tz_LANGUAGESCRIPT(key, "+languagename+") ";
                   sql1 += "            values (?, ?)  ";
                   val1 = key;
                   val2 = val;
                 } else{
                   sql1 =  "update tz_LANGUAGESCRIPT set "+languagename+"=? ";
                   sql1 += "  where key = ? ";
                   val1 = val;
                   val2 = key;
                 }
                 
                 pstmt = connMgr.prepareStatement(sql1);
                 if(!key.equals("")){
                   pstmt.setString(1,  val1);
                   pstmt.setString(2,  val2);
                   isOk = pstmt.executeUpdate();
                 }
                 if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
               }
             }

           if(isOk > 0){
               if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }
           }else{
               if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }
           }
        }
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
            if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.setAutoCommit(true); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }


    /**
    DB에서 파일로 저장 
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertSaveToFile(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        StringBuffer sb = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";        
        int isOk = 1;
        
        String key = "";
        String val = "";
        String languagename = box.getString("p_language");
        String dir = "d:/zpack/cresys_enterprise/WEB-INF/classes/conf/";
        String file = "";
        String dir_file = "";
        
        if(languagename.equals("KOREAN")){
            file = "ziaan_ko.properties";
        }else if(languagename.equals("ENGLISH")){
            file = "ziaan_en.properties";
        }else if(languagename.equals("JAPANESE")){
            file = "ziaan_jp.properties";
        }else if(languagename.equals("CHINESE")){
            file = "ziaan_ch.properties";
        }
        
        dir_file = dir+file;
        
        try {
           //ArrayList list1 = selectListLang(box);
           
           connMgr = new DBConnectionManager();
           
           
           //for(int i1=0; i1<list1.size(); i1++){
             //DataBox dbox1 =(DataBox)list1.get(i1);
             
/////////////PropertiesManager pt = PropertiesManager.getInstance(languagename);
/////////////Enumeration e = pt.getEnumeration();

             sql = "select key, "+languagename+" from tz_languagescript order by key";
             System.out.println(sql);
             ls = connMgr.executeQuery(sql);
             int i = 0;
             
             sb = new StringBuffer();
             
             while( ls.next()) {
              i=i+1;
              
               key = ls.getString("key");
               val = ls.getString(languagename);
               //val = pt.char_encoding(val);
               //System.out.println(key+"===>>>>"+val);
               sb.append(key+"="+val);
               //System.out.println(key+"===>>>>"+val);
		       sb.append("\r\n");
             }
             
             //파일 입력
		     
		     //Writer writer = null;
   		     //File jspFile = new File("C:/eclipse/workspace/SSU_eLearning/webapp/Web-INF/classes/conf/", "ziaan_ko.properties");
////             if(jspFile.isFile()) jspFile.delete();
////             java.io.FileWriter fw =  new java.io.FileWriter(jspFile.getAbsolutePath(), true);          
////             writer = new PrintWriter(new java.io.BufferedWriter(fw), true);
             //writer = new PrintWriter(new java.io.BufferedWriter(fw));

  			StringBuffer stringbuffer = (new StringBuffer()).append(dir_file);
  			//StringBuffer stringbuffer = (new StringBuffer()).append("C:/eclipse/workspace/SSU_eLearning/webapp/Web-INF/classes/conf/ziaan_ko.properties");
  			FileOutputStream fos = new  FileOutputStream(stringbuffer.toString(), false);
            //writer = new PrintWriter(fos, true);
            Writer writer =  new OutputStreamWriter(fos, "euc-kr");
   		    //writer.println("");
            writer.write(sb.toString());
            writer.flush();
////        fw.close();
            writer.close();
             
           //}

		   }
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            //if(writer != null) try{ writer.close(); }catch(Exception e1){ e1.printStackTrace(); }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
    
    
    /**
    등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insert(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        int isOk = 1;

        String v_prefix    = box.getString("p_prefix");
        String v_key       = box.getString("p_key");
        String v_korean    = box.getString("p_korean");
        String v_english   = box.getString("p_english");
        String v_japanese  = box.getString("p_japanese");
        String v_chinese   = box.getString("p_chinese");
        String v_comments   = box.getString("p_comments");
        
        String s_userid = box.getSession("userid");
        
        v_key = v_prefix + v_key;
        
        v_key = v_key.toLowerCase();

        try {
           connMgr = new DBConnectionManager();

           sql+= " INSERT INTO tz_LANGUAGESCRIPT (  \n";
           sql+= "    KEY, GUBUN, KOREAN,           \n";
           sql+= "    ENGLISH, JAPANESE, CHINESE,   \n";
           sql+= "    COMMENTS, ISUSED, LUSERID,    \n";
           sql+= "    LDATE, LIP)                   \n";
           sql+= " VALUES (                         \n";
           sql+= "     ?, ?, ?,                     \n";
           sql+= "     ?, ?, ?,                     \n";
           sql+= "     ?, 'Y', ?,                     \n";
           sql+= "     to_char(sysdate, 'YYYYMMDDHH24MISS'), ?)\n";

           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1,  v_key);
           pstmt.setString(2,  v_prefix);
           pstmt.setString(3,  v_korean);
           pstmt.setString(4,  v_english);
           pstmt.setString(5,  v_japanese);
           pstmt.setString(6,  v_chinese);
           pstmt.setString(7,  v_comments);
           pstmt.setString(8,  s_userid);
           pstmt.setString(9,  box.getSession("userip"));

           isOk = pstmt.executeUpdate();
		}
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
    
    
    
    
    
    /**
    수정
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int update(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        int isOk = 1;

        String v_prefix    = box.getString("p_prefix");
        String v_key       = box.getString("p_key");
        String v_korean    = box.getString("p_korean");
        String v_english   = box.getString("p_english");
        String v_japanese  = box.getString("p_japanese");
        String v_chinese   = box.getString("p_chinese");
        String v_comments   = box.getString("p_comments");
        
        String s_userid = box.getSession("userid");

        try {
           connMgr = new DBConnectionManager();
           
           sql =" update    \n";
           sql+="   tz_LANGUAGESCRIPT\n";
           sql+=" set\n";
           sql+="     KOREAN=?,\n";
           sql+="     ENGLISH=?,\n";
           sql+="     JAPANESE=?,\n";
           sql+="     CHINESE=?,\n";
           sql+="     COMMENTS=?,\n";
           sql+="     ISUSED='Y',\n";
           sql+="     LUSERID=?,\n";
           sql+="     LDATE = to_char(sysdate, 'YYYYMMDDHH24MISS')\n";
           sql+=" where\n";
           sql+="   key = ?\n";

           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1,  v_korean);
           pstmt.setString(2,  v_english);
           pstmt.setString(3,  v_japanese);
           pstmt.setString(4,  v_chinese);
           pstmt.setString(5,  v_comments);
           pstmt.setString(6,  s_userid);
           pstmt.setString(7,  v_key);

           isOk = pstmt.executeUpdate();
		}
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
    
    
    
    
    /**
    삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int delete(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        int isOk = 1;

        String v_prefix    = box.getString("p_prefix");
        String v_key       = box.getString("p_key");
        String v_korean    = box.getString("p_korean");
        String v_english   = box.getString("p_english");
        String v_japanese  = box.getString("p_japanese");
        String v_chinese   = box.getString("p_chinese");
        String v_comments   = box.getString("p_comments");
        
        String s_userid = box.getSession("userid");

        try {
           connMgr = new DBConnectionManager();
           
           sql =" delete from \n";
           sql+="   tz_LANGUAGESCRIPT\n";
           sql+=" where\n";
           sql+="   key = ?\n";

           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1,  v_key);

           isOk = pstmt.executeUpdate();
		}
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }
    
           
    
    /**
    언어 리스트
    @param box          receive from the form object and session
    @return ArrayList   메뉴 리스트
    */
    public ArrayList selectListLang(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;

        try {
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select languagename from tz_languagepack";
            //System.out.println(sql);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    



    /**
   선택 언어 리스트
    @param box          receive from the form object and session
    @return ArrayList   메뉴 리스트
    */
    public ArrayList selectListLangChoice(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;

        String v_key   = box.getString("p_key");

        try {
            
			connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select languagename from tz_languagepack";
            //System.out.println(sql);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
    
    /**
    * 
    * @param box          receive from the form object and session
    * @return int         
    * @throws Exception
    */
    public DataBox selectDuplicChk(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         ArrayList list	= null;
         DataBox dbox = null;
         list = new ArrayList();
         
         String sql = "";

		String v_key    = box.getString("p_key");
		String v_prefix = box.getString("p_prefix");
		
		v_key = v_prefix+v_key;
		
		v_key = v_key.toLowerCase();

         try {
             connMgr = new DBConnectionManager();

             sql = "select count(*) cnt ";
             sql+= " from tz_languagescript where key = '"+v_key+"'";
             
             System.out.println(sql);
             ls = connMgr.executeQuery(sql);
             
             if( ls.next()) {
                 dbox = ls.getDataBox();
             }

         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) {try {ls.close();} catch(Exception e){}}
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return dbox;
     }

 
	
}