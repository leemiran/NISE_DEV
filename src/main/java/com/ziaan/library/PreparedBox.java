//**********************************************************
//  1. ��      ��: �Ķ���� ����
//  2. ���α׷���: PreparedBox.java
//  3. ��      ��: request ��ü�� ���Ͽ� �Ѿ���� �Ķ���͸� box hashtable ���� �����Ѵ�
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2003. 4. 26
//  7. ��      ��: ������ 2003. 4. 26
//**********************************************************

package com.ziaan.library;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.lang.reflect.Array;
import javax.servlet.http.HttpSession;

/**
* request ���� �Ѿ���� �Ķ���Ϳ� session ��ü�� Hashtable �� box ��ü�� ��� �����Ѵ�
* @date   : 2003. 5
* @author : j. h. lee
*/
public class PreparedBox extends java.util.Hashtable {
    protected String name = null;
    /**
    *Hashtable ���� �����Ѵ�.
    */
    public PreparedBox(String name) {
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
    * box ��ü�� ������ִ� ��� key, value �� String Ÿ������ ��ȯ�Ѵ�. 
    @return String ��� key, value �� String Ÿ������ ��ȯ�Ѵ�. 
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
        
        return "DataBox["+name+"]=" + buf.toString();
    }
     /**
    Hashtable�� Integer �������� set
    * @param int key
    * @param int value
    */    
    public void setInt(int key, int  value) {
        super.put(new Integer(key), new Integer(value));
    }
    /**
    Hashtable�� Integer �������� set
    * @param int key
    * @param String value
    */   
    public void setString(int key, String  value) {
        super.put(new Integer(key), new String( value));
    }
    /**
    Hashtable�� Float �������� set
    * @param int key
    * @param float value
    */  
    public void setFloat(int key, float value) {
        super.put(new Integer(key), new Float(value));
    }
    /**
    Hashtable�� Double �������� set
    * @param int key
    * @param double value
    */  
    public void setDouble(int key, double  value) {
        super.put(new Integer(key), new Double(value));
    }
    /**
    Hashtable�� Long �������� set
    * @param int key
    * @param long value
    */  
    public void setLong(int key, long  value) {
        super.put(new Integer(key), new Long(value));
    }
    /**
    Hashtable�� Voctor �������� set
    * @param int key
    * @param Vector value
    */  
    public void setVector(int key, Vector value) {
        super.put(new Integer(key), value);
    }
    /**
    Hashtable�� Clob �������� set
    * @param String sql
    * @param String value
    */  
    public void setClob(String sql, String value) {
        super.put(new String(sql),  new String(value));
    }
}
