
package com.ziaan.library;

import java.lang.reflect.Array;
import java.util.Enumeration;

/**
 * <p> 제목: DataBox</p> 
 * <p> 설명: select sql 에 의해 출력되는 data 를  DataBox hashtable 에서 관리한다</p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class DataBox extends java.util.Hashtable { 
    protected String name = null;
    /**
    *Hashtable 명을 설정한다.
    */
    public DataBox(String name) { 
        super();
        this.name = name;
    } 
    
     /**
    * box 객체에 담긴 param value 의 String 타입을 얻는다.
    @param key param key
    @return String value 를 반환한다.
    */
    public String get(String key) { 
        return getString(key);
    }
    
     /**
    * box 객체에 담긴 param value 의 boolean 타입을 얻는다.
    @param key param key
    @return boolean value 를 반환한다.
    */
    public boolean getBoolean(String key) { 
        String value = getString(key);
        boolean isTrue = false;
        try { 
            isTrue = (new Boolean(value)).booleanValue();
        } catch( Exception e ) { }

        return isTrue;
    }
    
     /**
    * box 객체에 담긴 param value 의 double 타입을 얻는다.
    @param key param key
    @return double value 를 반환한다.
    */
    public double getDouble(String key) { 
        String value = removeComma(getString(key));
        if ( value.equals("") ) return 0;
        double num = 0;
        try { 
            num = Double.valueOf(value).doubleValue();
        } catch( Exception e ) { 
            num = 0;
        }

        return num;
    }

     /**
    * box 객체에 담긴 param value 의 float 타입을 얻는다.
    @param key param key
    @return float value 를 반환한다.
    */
    public float getFloat(String key) { 
        return (float)getDouble(key);
    }
    
     /**
    * box 객체에 담긴 param value 의 int 타입을 얻는다.
    @param key param key
    @return int value 를 반환한다.
    */
    public int getInt(String key) { 
        double value = getDouble(key);
        return (int)value;
    }
    
     /**
    * box 객체에 담긴 param value 의 long 타입을 얻는다.
    @param key param key
    @return long value 를 반환한다.
    */
    public long getLong(String key) { 
        String value = removeComma(getString(key));
        if ( value.equals("") ) return 0L;
        
        long lvalue = 0L;
        try { 
            lvalue = Long.valueOf(value).longValue();
        } catch( Exception e ) { 
            lvalue = 0L;
        }       
        return lvalue;
    }
    
     /**
    * box 객체에 담긴 param value 의 String 타입을 얻는다.
    @param key param key
    @return String value 를 반환한다.
    */
    public String getString(String key) { 
        String value = null;
        try { 
            Object o = super.get(key);
            Class c = o.getClass();
            if ( o == null ) { 
                value = "";
            } else if ( c.isArray() ) { 
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
        } catch( Exception e ) { 
        value = "";
        }

        return value;
    }
    
    /**
    * box 객체에 담긴 param value 의 String 타입을 얻는다.
    @param key param key
    @return String value 를 반환한다.
    */
    public Object getObject(String key) { 
        Object value = null;
        try { 
            value = (Object)super.get(key);
        } catch( Exception e ) { 
            value = null;
        }

        return value;
    }
    
    private static String removeComma(String s) { 
        if ( s == null ) return null;
        if ( s.indexOf(",") != -1 ) { 
            StringBuffer buf = new StringBuffer();
            for ( int i = 0;i<s.length();i++ ) { 
                char c = s.charAt(i);
                if ( c != ',') buf.append(c);
            }
            return buf.toString();
        }

        return s;
    }
    
    /**
    * box 객체에 담겨져있는 모든 key, value 를 String 타입으로 반환한다. 
    @return String 모든 key, value 를 String 타입으로 반환한다. 
    */
    public synchronized String toString() { 
        int max = size() - 1;
        StringBuffer buf = new StringBuffer();
        Enumeration keys = keys();
        Enumeration objects = elements();
        buf.append("{ ");
        
        for ( int i = 0; i <= max; i++ ) { 
            String key = keys.nextElement().toString();
            String value = null;
            Object o = objects.nextElement();
            if ( o == null ) { 
                value = "";
            }else { 
                Class  c = o.getClass();
                if ( c.isArray() ) { 
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
                        for ( int j=0;j<length;j++ ) { 
                            Object item = Array.get(o, j);
                            if ( item != null ) valueBuf.append(item.toString() );
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
            if ( i < max) buf.append(", ");
        }
        buf.append("}");
        
        return "DataBox[" +name + "]=" + buf.toString();
    }
}
