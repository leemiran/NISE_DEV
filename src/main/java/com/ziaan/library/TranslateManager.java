 /**
 * 제목: 다국어 지원 라이브러리
 * 설명: 
 * Copyright: Copyright (c) 2006
 * Company:  
 *@author 이창훈
 *@date 2006. 01
 *@version 1.0
 */

package com.ziaan.library;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;



public class TranslateManager {
    
	private String v_language="KOREAN";
	public String v_directory="";
	public String v_st1="";
	public String v_st2="";
	public String v_CharSet="";
	
	private String s_languageName="";
	private String v_languageName="";
	
    Locale lo_lang = null;
	
	RequestBox box=null;

	HttpSession session = null;

    void setBox(RequestBox box){
		this.box = box;
	}
//    Locale lo_lang = new Locale("en", "US");

	public TranslateManager(RequestBox box) {

	  this.box = box;
      
	  try{
//		ConfigSet config = new ConfigSet();
//      v_language = config.getProperty("ziaan.language.value");
 //       lo_lang = new Locale("en", "US");
		v_language = box.getSession("languageName");

		if(v_language.equals("")){
		  v_language = "KOREAN";
		}

//	    System.out.println("v_language:"+v_language);

		//v_language = getLanguage();
		setLanguageVariable(v_language);
//		setLangVariable("ENGLISH");
	  }
      catch(Exception e) {
          e.printStackTrace();
      }
    } 

	public TranslateManager() {

      
	  try{
//		ConfigSet config = new ConfigSet();
//      v_language = config.getProperty("ziaan.language.value");
		//v_language = getLanguage();

//		v_language = session.getAttribute("languageName").toString();
//		System.out.println("result==========================================>"+v_language);
		setLanguageVariable(v_language);
//		setLangVariable("ENGLISH");
	  }
      catch(Exception e) {
          e.printStackTrace();
      }
    } 


	
	public String TranslateMessage(String strCode) {
		DBConnectionManager connMgr = null;
        ListSet ls          = null;
		String result="";
		String rtn = null;
		
		String sql = "";
	
		try{
				
              ResourceBundle bundle =ResourceBundle.getBundle(v_directory, lo_lang);  
    		  result =  bundle.getString(strCode);
			  rtn =  new String(result.getBytes(v_st1),v_st2);
			  /*
			 connMgr = new DBConnectionManager();
			 sql = "select "+v_language+" from tu_languagescript where key = '"+strCode+"'";
			 ls = connMgr.executeQuery(sql);
			 
			 if(ls.next()){
			   result = ls.getString(v_language);
			 }
			 System.out.println("result====>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+result);
			  */
		}catch(Exception ex) {
            
        }finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
		return rtn;
		//return result;
    }


	public void setLanguageVariable(String v_language) {
	    //System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk===>>"+v_language);
	    
      
	   if(v_language.equals("ENGLISH")){
          v_directory = "conf.ziaan_en";
          lo_lang = Locale.ENGLISH;
		  v_st1 = "iso8859-1";
		  v_st2 = "UTF-8";
        } 

		else if (v_language.equals("KOREAN")){
          v_directory = "conf.ziaan_ko";
          lo_lang = Locale.KOREAN;
		  v_st1 = "iso8859-1";
 		  v_st2 = "euc-kr";
        }

		else if (v_language.equals("JAPANESE")){
          v_directory = "conf.ziaan_jp";
          lo_lang = Locale.JAPANESE;
		  v_st1 = "iso8859-1"; //8859_1
 		  v_st2 = "UTF-8";  // File Encoding 방법에 따라  ( Properties File )
        }

		else if (v_language.equals("CHINESE")){
          v_directory = "conf.ziaan_ch";
          lo_lang = Locale.CHINESE;
		  v_st1 = "iso8859-1"; //8859_1
 		  v_st2 = "UTF-8"; // File Encoding 방법에 따라
        }
	   
	   //System.out.println("v_directory===>>>"+v_directory);

	}

}


