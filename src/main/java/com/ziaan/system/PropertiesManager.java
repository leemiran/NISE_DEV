//**********************************************************
//  1. 제      목: 
//  2. 프로그램명: PropertiesManager.java
//  3. 개      요: 
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0 QnA
//  6. 작      성: 이창훈 2006.1.31
//  7. 수      정:
//**********************************************************

package com.ziaan.system;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesManager  { 

    //private static final String propFile = "D:/zpack/multilms/Web-INF/classes/conf/test.properties"; 
    private String propFile = "/conf/ziaan_ko.properties"; 
    private String propDir = "D:/zpack/cresys_enterprise/WEB-INF/classes"; 
    
    private String PropertiesManagerHeader = "${key}=${val}"; 

    private static PropertiesManager propTest = null; 

    private static Properties p; 
    
    public PropertiesManager() { 
    }  
    
    public PropertiesManager(String language) { 
      java.io.InputStream i = null; 
      p = null; 
      if(language.equals("KOREAN")){
        propFile = "/conf/ziaan_ko.properties"; 
      }else if(language.equals("ENGLISH")){
        propFile = "/conf/ziaan_en.properties"; 
      }else if(language.equals("JAPANESE")){
        propFile = "/conf/ziaan_jp.properties"; 
      }else if(language.equals("CHINESE")){
        propFile = "/conf/ziaan_zh.properties"; 
      }
      
      i = this.getClass().getClassLoader().getResourceAsStream(propFile); 
      
      try { 
          p = new Properties(); 
          p.load(i); 
          i.close(); 
      } catch (java.io.IOException e) { 
          p = null; 
          System.out.println(e.getMessage()); 
      } 
      finally { 
          i = null; 
      } 
    }
/*
    public static void main(String args[]) { 
      //System.out.println("hello");
      PropertiesManager pt = PropertiesManager.getInstance(); 
      Enumeration e = p.propertyNames(); 
      
      while (e.hasMoreElements()) { 
        String key = (String)e.nextElement(); 
        String val = (String)p.get(key); 
        String newVal = Integer.toString(Integer.parseInt(val) + 1); 
        pt.setVal(key, newVal);  
        System.out.println("key : " + key + "  old val : " + val+ "  new val : " + newVal); 
      } 
    } 
*/   
    // singleton 
    public static PropertiesManager getInstance(String language) { 
      //if (null==propTest) { 
        propTest = new PropertiesManager(language); 
      //} 
      return propTest; 
    }

    public void setVal(String key, String val) { 
        try { 
            p.put(key, val); 
            System.out.println("OKOKOK 1");
            FileOutputStream fileoutstream = new FileOutputStream(propDir+propFile);
            p.store(fileoutstream,  PropertiesManagerHeader); 
            System.out.println("OKOKOK2");
        } 
        catch (IOException e) { 
       } 
    } 
    
    public Enumeration getEnumeration() { 
      return p.propertyNames();
    } 
    
    public String getKeyValue(String key) { 
      //return char_encoding((String)p.get(key));
      return (String)p.get(key);
    } 
    
    public String getKeyValueEncode(String key) { 
      return char_encoding((String)p.get(key));
      //return (String)p.get(key);
    } 
    
    //utf-8로 인코딩
    public static String char_encoding(String str){
      String rtn = null;
      try{
        if(str != null){
          rtn = new String(str.getBytes("8859_1"), "EUC_KR");
        }
      }catch(java.io.UnsupportedEncodingException e){}
      return rtn;
    }
    
    //유니코드를 한글로 변환
    public static String KoreaToUnicode(String str){
      String rtn = null;
      try{
        if(str != null){
          rtn = new String(str.getBytes("8859_1"),"KSC5601");
        }
      }catch(java.io.UnsupportedEncodingException e){}
      return rtn;
    }
    
}