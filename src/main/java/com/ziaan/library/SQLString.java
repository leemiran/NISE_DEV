
package com.ziaan.library;

import com.ziaan.library.StringManager;

 /**
 * <p> ����: SQL ����ó�� ���� ���̺귯��</p> 
 * <p> ����: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author ������
 *@date 2003. 12
 *@version 1.0
 */
public class SQLString { 
    /**
    * SQL ���� ������ '   ' �� �ٿ� �����Ѵ�.
    @param str  ����
    @return 'str'
    */
    public static String Format(String str) {
    	String result = "";
        if ( str != null ) 
            str = StringManager.replace(str, "--", "");
            str = StringManager.replace(str, "+", "");
            //str = replace(str, "/", "");
            str = StringManager.replace(str, "\\", "");
            str = StringManager.replace(str, "&", "");
            //str = StringManager.replace(str, "*", "");
            
            result = "'" + StringManager.chkNull(StringManager.replace(str, "'", "")) + "'";
        return 	("'" + str + "'");
    }
    
    public static String Format(int i) { 
        return	String.valueOf(i);
    }
    
    public static String Format(double d) { 
        return	String.valueOf(d);
    }  
}
