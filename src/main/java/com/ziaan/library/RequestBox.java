//**********************************************************
//  1. 제      목: 파라미터 저장
//  2. 프로그램명: RequestBox.java
//  3. 개      요: request 객체를 통하여 넘어오는 파라미터를 box hashtable 에서 관리한다
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 이정한 2003. 4. 26
//  7. 수      정: 이정한 2003. 4. 26
//**********************************************************

package com.ziaan.library;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpSession;

/**
* request 에서 넘어오는 파라미터와 session 객체를 Hashtable 인 box 객체에 담아 관리한다
* @date   : 2003. 5
* @author : j. h. lee
*/
public class RequestBox extends java.util.Hashtable {
    protected String name = null;
    TranslateManager tm = null;  // 다국어지원
    
    /**
    *Hashtable 명을 설정한다.
    */
    public RequestBox(String name) {
    super();
    this.name = name;
    } 
    
     /**
    box 객체에 담긴 parameter value 의 String 타입을 얻는다.
    * @param key parameter key
    * @return String value 를 반환한다.
    */
    public String get(String key) {
        return getString(key);
    }
    
     /**
    box 객체에 담긴 parameter value 의 boolean 타입을 얻는다.
    * @param key parameter key
    * @return boolean value 를 반환한다.
    */
    public boolean getBoolean(String key) {
        String value = getString(key);
        boolean isTrue = false;
        try {
            isTrue = (new Boolean(value)).booleanValue();
        }
        catch(Exception e){}
        return isTrue;
    }
    
     /**
    box 객체에 담긴 parameter value 의 double 타입을 얻는다.
    * @param key parameter key
    * @return double value 를 반환한다.
    */
    public double getDouble(String key) {
        String value = removeComma(getString(key));
        if ( value.equals("") ) return 0;
        double num = 0;
        try {
            num = Double.valueOf(value).doubleValue();
        }
        catch(Exception e) {
            num = 0;
        }
        return num;
    }

     /**
    box 객체에 담긴 parameter value 의 float 타입을 얻는다.
    * @param key parameter key
    * @return float value 를 반환한다.
    */
    public float getFloat(String key) {
        return (float)getDouble(key);
    }
    
     /**
    box 객체에 담긴 parameter value 의 int 타입을 얻는다.
    * @param key parameter key
    * @return int value 를 반환한다.
    */
    public int getInt(String key) {
        double value = getDouble(key);
        return (int)value;
    }
    
     /**
    box 객체에 담긴 parameter value 의 long 타입을 얻는다.
    * @param key parameter key
    * @return long value 를 반환한다.
    */
    public long getLong(String key) {
        String value = removeComma(getString(key));
        if ( value.equals("") ) return 0L;
        
        long lvalue = 0L;
        try {
            lvalue = Long.valueOf(value).longValue();
        }
        catch(Exception e) {
            lvalue = 0L;
        }       
        return lvalue;
    }
    
     /**
    box 객체에 담긴 parameter value 의 String 타입을 얻는다.
    * @param key parameter key
    * @return String value 를 반환한다.
    */
    public String getString(String key) {
        String value = null;
        try {
            Object o = (Object)super.get(key);
            Class c = o.getClass();
            if ( o == null ) {
                value = "";
            }
            else if( c.isArray() ) {
                int length = Array.getLength(o);
                if ( length == 0 ) value = "";
                else {
                    Object item = Array.get(o, 0);
                    if ( item == null ) value = "";
                    else value = item.toString();
                }
            }			
            else {
                value = o.toString();
            }
        }
        catch(Exception e) {
        value = "";
        }
        return value;
    }
    
    /**
    box 객체에 담긴 parameter value 의 String 타입을 얻는다.
    * @param key parameter key
    * @return String value 를 반환한다.
    */
    public Object getObject(String key) {
        Object value = null;
        try {
            value = (Object)super.get(key);
        }
        catch(Exception e) {
            value = null;
        }
        return value;
    }
    /**
    * checkbox와 같이 동일한 key 에 value 를 여러개 선택하여 넘길 경우, 각 선택된 value의 list를 Vector에 담아 반환한다.
    * @param key parameter key
    * @return vector parameter values 를 담은 Vector 를 반환한다.
    */
    public Vector getVector(String key) {
        Vector vector = new Vector();
        try {
            Object o = (Object)super.get(key);
            Class c = o.getClass();
            if ( o != null ) {
                if( c.isArray() ) {
                    int length = Array.getLength(o);
                    if ( length != 0 ) {
                        for(int i=0; i<length;i++) {	
                            Object tiem = Array.get(o, i);
                            if (tiem == null ) vector.addElement("");
                            else vector.addElement(tiem.toString());
                        }
                    }
                }
                else if(o instanceof Vector) {
                    Vector v = (Vector)o;
                    for(int i=0; i<v.size();i++) {	
                        vector.addElement((String)v.elementAt(i));
                    }
                }
                else {
                    vector.addElement(o.toString());
                }           
            }
        }
        catch(Exception e) {}
        return vector;
    }
    /**
    box 객체에 담긴 parameter value 의 String 타입을 얻는다.
    key에 해당하는 값이 없으면 defstr을 반환한다.
    * @param key parameter key
    * @return String value 를 반환한다.
    */
    public String getStringDefault(String key, String defstr) {
        return (getString(key).equals("") ? defstr : getString(key));
    }
    
     /**
    box 객체에 담긴 업로드된 원파일명을 반환한다. (단수)
    * @param key parameter key
    * @return String 업로드된 파일의 원래 이름을 반환한다.
    */
    public String getRealFileName(String key) {
        String realname = "";
        Vector v = (Vector)getObject(key + "_real");
        if(v != null) {
            for(int i = 0; i < v.size(); i++) {
                String tmp = (String)v.elementAt(i);
                if(tmp != null) {
                    int idx = tmp.indexOf("|");
                    String name = tmp.substring(0, idx);
                    String filename = tmp.substring(idx+1);
                    if(key.equals(name)) {
                        realname = filename;
                    }
                }
            }
        }
        return realname;
    }
    
     /**
    box 객체에 담겨 하드에 저장되는 새로운 업로드된 파일명을 반환한다. (단수)
    * @param key parameter key
    * @return String 업로드된 파일의 새로운 이름을 반환한다.
    */
    public String getNewFileName(String key) {
        String newname = "";
        Vector v = (Vector)getObject(key + "_new");
        if(v != null) {
            for(int i = 0; i < v.size(); i++) {
                String tmp = (String)v.elementAt(i);
                if(tmp != null) {
                    int idx = tmp.indexOf("|");
                    String name = tmp.substring(0, idx);
                    String filename = tmp.substring(idx+1);
                    if(key.equals(name)) {
                        newname = filename;
                    }
                }
            }
        }
        return newname;
    }
    
    /**
    box 객체에 담긴 업로드된 원파일명들을 반환한다. (복수)
    * @param key parameter key
    * @return Vector 업로드된 파일의 원래 이름들을 반환한다.
    */
    public Vector getRealFileNames(String key) {
        Vector realVector = new Vector();
        Vector v = (Vector)getObject(key + "_real");
        if(v != null) {
            for(int i = 0; i < v.size(); i++) {
                String tmp = (String)v.elementAt(i);
                if(tmp != null) {
                    int idx = tmp.indexOf("|");
                    String name = tmp.substring(0, idx);
                    String filename = tmp.substring(idx+1);
                    if(key.equals(name)) {
                        realVector.addElement(filename);
                    }
                }
            }
        }
        return realVector;
    }
    
     /**
    box 객체에 담겨 하드에 저장되는 새로운 업로드된 파일명들을 반환한다. (복수)
    * @param key parameter key
    * @return Vector 업로드된 파일의 새로운 이름들을 반환한다.
    */
    public Vector getNewFileNames(String key) {
        Vector newVector = new Vector();
        Vector v = (Vector)getObject(key + "_new");
        if(v != null) {
            for(int i = 0; i < v.size(); i++) {
                String tmp = (String)v.elementAt(i);
                if(tmp != null) {
                    int idx = tmp.indexOf("|");
                    String name = tmp.substring(0, idx);
                    String filename = tmp.substring(idx+1);
                    if(key.equals(name)) {
                        newVector.addElement(filename);
                    }
                }
            }
        }
        return newVector;
    }
 
    private static String removeComma(String s) {
        if ( s == null ) return null;
        if ( s.indexOf(",") != -1 ) {
            StringBuffer buf = new StringBuffer();
            for(int i=0;i<s.length();i++){
                char c = s.charAt(i);
                if ( c != ',') buf.append(c);
            }
            return buf.toString();
        }
        return s;
    }
    
    /**
    box 객체에 담겨져있는 모든 key, value 를 String 타입으로 반환한다. 
    * @return String 모든 key, value 를 String 타입으로 반환한다. 
    */
    public synchronized String toString() {
        int max = size() - 1;
        StringBuffer buf = new StringBuffer();
        Enumeration keys = keys();
        Enumeration objects = elements();
        buf.append("{");
        
        for (int i = 0; i <= max; i++) {
            String key = keys.nextElement().toString();
            String value = null;
            Object o = objects.nextElement();
            if ( o == null ) {
                value = "";
            }else {
                Class  c = o.getClass();
                if( c.isArray() ) {
                    int length = Array.getLength(o);
                    
                    if ( length == 0 ) 	{
                        value = "";
                    }
                    else if ( length == 1 ) {
                        Object item = Array.get(o, 0);
                        if ( item == null ) value = "";
                        else value = item.toString();
                    }
                    else {
                        StringBuffer valueBuf = new StringBuffer();
                        valueBuf.append("[");
                        for ( int j=0;j<length;j++) {
                            Object item = Array.get(o, j);
                            if ( item != null ) valueBuf.append(item.toString());
                            if ( j<length-1) valueBuf.append(",");
                        }
                        valueBuf.append("]");
                        value = valueBuf.toString();
                    }
                }
                else {
                    value = o.toString();
                }           
            }
            buf.append(key + "=" + value);
            if (i < max) buf.append(", ");
        }
        buf.append("}");
        
        return "RequestBox["+name+"]=" + buf.toString();
    }
    
    /**
    box 객체에 담긴 session 객체를 반환한다.
    * @return session 
    */
    public HttpSession getHttpSession() {
        HttpSession session = null;
        try {
            session = (HttpSession)super.get("session");
        }
        catch(Exception e) {
        }
        return session;
    }
    
    /**
    String 타입의 세션변수을 저장한다.
    * @param key String
    * @param s_value String
    */
    public void setSession(String key, String s_value) {
        HttpSession session = this.getHttpSession();//System.out.println("setSession " + session);
        if(session != null) {
            session.setAttribute(key, s_value);
        }
    }

    /**
    int 타입의 세션변수을 저장한다.
    * @param key String
    * @param s_value int
    */
    public void setSession(String key, int i_value) {
        HttpSession session = this.getHttpSession();
        if(session != null)	{
            session.setAttribute(key, new Integer(i_value));
        }
    }

    /**
    String 타입의 세션변수을 가지고온다.
    * @param key String
    * @return s_value String
    */
    public String getSession(String key) {
        HttpSession session = this.getHttpSession();//System.out.println("getSession " + session);
        String s_value = "" ;
        if (session != null) {
	        Enumeration e2 = session.getAttributeNames();
	        while(e2.hasMoreElements()){
	            String name = (String)e2.nextElement();//System.out.println("Sessionkey " + name);     
	        }
	           
	        if( session != null ) {
	            Object obj = session.getAttribute(key);    //  System.out.println("getSession obj" + obj);     
	            if ( obj != null) {
	                s_value = obj.toString();
	            }
	        }
        }
        return s_value ;
    }

    /**
    int 타입의 세션변수을 가지고온다.(해당값이 없을때 default 로 돌려줄 값을 파라메터로 넘겨받아야 한다.)
    * @param key String
    * @param defaultValue int
    * @return s_value int
    */
    public int getSession(String key, int defaultValue) {
        int i_value = defaultValue;
        
        String s_value = this.getSession(key);
        
        if (!s_value.equals("")) {
            try {
                i_value = Integer.parseInt(s_value);
            } catch (Exception ex) {
                i_value = defaultValue;
            }
        }
        return i_value;
    }

   /**
    세션id 를 얻는다.
    * @return sessionId String
    */
    public String getSessionId() {
        HttpSession session = this.getHttpSession();
        String sessionId = "" ;
        
        if( session != null ) {
            sessionId = session.getId();
        }
        return sessionId;
    }

    /** 
    현재 사용자가 세션을 가지고 있는지 여부. (세션변수에 userid 가 공백이 아닌지 여부로 판단.. )
    * @param key String userid
    * @return boolean userid 를 가지고 있으면 true 를 반환한다.
    */
    public boolean hasSession(String key) {
        return !getSession(key).equals("");
    }
    
//  다국어지원 Box Manager 

	public void createTranslate(){
		tm = new TranslateManager(this);  
	}
	  
     /**
    * 번역 (다국어지원)
    @return script String
    */
    public String getTranslate(String strCode) {
		tm = new TranslateManager();
        return tm.TranslateMessage(strCode);
    }
}
