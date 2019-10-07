
package com.ziaan.library;

import java.lang.reflect.Array;
import java.util.Enumeration;

/**
 * <p> ����: DataBox</p> 
 * <p> ����: select sql �� ���� ��µǴ� data ��  DataBox hashtable ���� �����Ѵ�</p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author ������
 *@date 2003. 12
 *@version 1.0
 */
public class DataBox extends java.util.Hashtable { 
    protected String name = null;
    /**
    *Hashtable ���� �����Ѵ�.
    */
    public DataBox(String name) { 
        super();
        this.name = name;
    } 
    
     /**
    * box ��ü�� ��� param value �� String Ÿ���� ��´�.
    @param key param key
    @return String value �� ��ȯ�Ѵ�.
    */
    public String get(String key) { 
        return getString(key);
    }
    
     /**
    * box ��ü�� ��� param value �� boolean Ÿ���� ��´�.
    @param key param key
    @return boolean value �� ��ȯ�Ѵ�.
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
    * box ��ü�� ��� param value �� double Ÿ���� ��´�.
    @param key param key
    @return double value �� ��ȯ�Ѵ�.
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
    * box ��ü�� ��� param value �� float Ÿ���� ��´�.
    @param key param key
    @return float value �� ��ȯ�Ѵ�.
    */
    public float getFloat(String key) { 
        return (float)getDouble(key);
    }
    
     /**
    * box ��ü�� ��� param value �� int Ÿ���� ��´�.
    @param key param key
    @return int value �� ��ȯ�Ѵ�.
    */
    public int getInt(String key) { 
        double value = getDouble(key);
        return (int)value;
    }
    
     /**
    * box ��ü�� ��� param value �� long Ÿ���� ��´�.
    @param key param key
    @return long value �� ��ȯ�Ѵ�.
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
    * box ��ü�� ��� param value �� String Ÿ���� ��´�.
    @param key param key
    @return String value �� ��ȯ�Ѵ�.
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
    * box ��ü�� ��� param value �� String Ÿ���� ��´�.
    @param key param key
    @return String value �� ��ȯ�Ѵ�.
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
    * box ��ü�� ������ִ� ��� key, value �� String Ÿ������ ��ȯ�Ѵ�. 
    @return String ��� key, value �� String Ÿ������ ��ȯ�Ѵ�. 
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
