//**********************************************************
//  1. ��      ��: �Ķ���� ����
//  2. ���α׷���: RequestBox.java
//  3. ��      ��: request ��ü�� ���Ͽ� �Ѿ���� �Ķ���͸� box hashtable ���� �����Ѵ�
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2003. 4. 26
//  7. ��      ��: ������ 2003. 4. 26
//**********************************************************

package com.ziaan.library;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpSession;

/**
* request ���� �Ѿ���� �Ķ���Ϳ� session ��ü�� Hashtable �� box ��ü�� ��� �����Ѵ�
* @date   : 2003. 5
* @author : j. h. lee
*/
public class RequestBox extends java.util.Hashtable {
    protected String name = null;
    TranslateManager tm = null;  // �ٱ�������
    
    /**
    *Hashtable ���� �����Ѵ�.
    */
    public RequestBox(String name) {
    super();
    this.name = name;
    } 
    
     /**
    box ��ü�� ��� parameter value �� String Ÿ���� ��´�.
    * @param key parameter key
    * @return String value �� ��ȯ�Ѵ�.
    */
    public String get(String key) {
        return getString(key);
    }
    
     /**
    box ��ü�� ��� parameter value �� boolean Ÿ���� ��´�.
    * @param key parameter key
    * @return boolean value �� ��ȯ�Ѵ�.
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
    box ��ü�� ��� parameter value �� double Ÿ���� ��´�.
    * @param key parameter key
    * @return double value �� ��ȯ�Ѵ�.
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
    box ��ü�� ��� parameter value �� float Ÿ���� ��´�.
    * @param key parameter key
    * @return float value �� ��ȯ�Ѵ�.
    */
    public float getFloat(String key) {
        return (float)getDouble(key);
    }
    
     /**
    box ��ü�� ��� parameter value �� int Ÿ���� ��´�.
    * @param key parameter key
    * @return int value �� ��ȯ�Ѵ�.
    */
    public int getInt(String key) {
        double value = getDouble(key);
        return (int)value;
    }
    
     /**
    box ��ü�� ��� parameter value �� long Ÿ���� ��´�.
    * @param key parameter key
    * @return long value �� ��ȯ�Ѵ�.
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
    box ��ü�� ��� parameter value �� String Ÿ���� ��´�.
    * @param key parameter key
    * @return String value �� ��ȯ�Ѵ�.
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
    box ��ü�� ��� parameter value �� String Ÿ���� ��´�.
    * @param key parameter key
    * @return String value �� ��ȯ�Ѵ�.
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
    * checkbox�� ���� ������ key �� value �� ������ �����Ͽ� �ѱ� ���, �� ���õ� value�� list�� Vector�� ��� ��ȯ�Ѵ�.
    * @param key parameter key
    * @return vector parameter values �� ���� Vector �� ��ȯ�Ѵ�.
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
    box ��ü�� ��� parameter value �� String Ÿ���� ��´�.
    key�� �ش��ϴ� ���� ������ defstr�� ��ȯ�Ѵ�.
    * @param key parameter key
    * @return String value �� ��ȯ�Ѵ�.
    */
    public String getStringDefault(String key, String defstr) {
        return (getString(key).equals("") ? defstr : getString(key));
    }
    
     /**
    box ��ü�� ��� ���ε�� �����ϸ��� ��ȯ�Ѵ�. (�ܼ�)
    * @param key parameter key
    * @return String ���ε�� ������ ���� �̸��� ��ȯ�Ѵ�.
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
    box ��ü�� ��� �ϵ忡 ����Ǵ� ���ο� ���ε�� ���ϸ��� ��ȯ�Ѵ�. (�ܼ�)
    * @param key parameter key
    * @return String ���ε�� ������ ���ο� �̸��� ��ȯ�Ѵ�.
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
    box ��ü�� ��� ���ε�� �����ϸ���� ��ȯ�Ѵ�. (����)
    * @param key parameter key
    * @return Vector ���ε�� ������ ���� �̸����� ��ȯ�Ѵ�.
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
    box ��ü�� ��� �ϵ忡 ����Ǵ� ���ο� ���ε�� ���ϸ���� ��ȯ�Ѵ�. (����)
    * @param key parameter key
    * @return Vector ���ε�� ������ ���ο� �̸����� ��ȯ�Ѵ�.
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
    box ��ü�� ������ִ� ��� key, value �� String Ÿ������ ��ȯ�Ѵ�. 
    * @return String ��� key, value �� String Ÿ������ ��ȯ�Ѵ�. 
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
    box ��ü�� ��� session ��ü�� ��ȯ�Ѵ�.
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
    String Ÿ���� ���Ǻ����� �����Ѵ�.
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
    int Ÿ���� ���Ǻ����� �����Ѵ�.
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
    String Ÿ���� ���Ǻ����� ������´�.
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
    int Ÿ���� ���Ǻ����� ������´�.(�ش簪�� ������ default �� ������ ���� �Ķ���ͷ� �Ѱܹ޾ƾ� �Ѵ�.)
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
    ����id �� ��´�.
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
    ���� ����ڰ� ������ ������ �ִ��� ����. (���Ǻ����� userid �� ������ �ƴ��� ���η� �Ǵ�.. )
    * @param key String userid
    * @return boolean userid �� ������ ������ true �� ��ȯ�Ѵ�.
    */
    public boolean hasSession(String key) {
        return !getSession(key).equals("");
    }
    
//  �ٱ������� Box Manager 

	public void createTranslate(){
		tm = new TranslateManager(this);  
	}
	  
     /**
    * ���� (�ٱ�������)
    @return script String
    */
    public String getTranslate(String strCode) {
		tm = new TranslateManager();
        return tm.TranslateMessage(strCode);
    }
}
