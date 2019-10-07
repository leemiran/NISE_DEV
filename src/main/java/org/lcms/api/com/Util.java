/**
* @(#)Util.java
* description :  LCMS 공통으로 사용되는 Class.
* note : 
* date : 2008. 12. 31.
* @author : 4CSOFT-오일환.
* @version : 1.0.
* modified : 
* comment : 
...
*/
package org.lcms.api.com;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class Util {

	static String chararray="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	static Random random=new Random(System.currentTimeMillis());	
    /**
     * null인지 체크후 boolean으로 리턴
     */ 
	public static boolean isNull(String val){
		if(val == null || val.equals("") || val.equals("null"))
			return true;
		else return false;
	}
    /**
     * null을 ""을 바꿔 리턴
     */ 	
	 public static String NullCheck(String val)
    {
        if(val == null)
            return "";
        else
            return val;
    }
	    /**
	     * null을 val1을 바꿔 리턴
	     */ 
	public static String NullCheck(String val, String val1)
	{
		if(val == null  || val.equals("null") ||"".equals(val))
			return val1;
		else
			return val;
	}
    /**
     * null을 val1을 바꿔 리턴
     */ 
	public static String NullCheck(String val, int val1)
	{
		if(val == null)
			return val1+"";
		else
			return val;
	}
    /**
     * null을 ""을 바꿔 리턴
     */ 
	public static String NullCheck(Object val)
	{
		if (val == null)
			return "";
		else
			return val.toString();
	}
    /**
     * 날짜타입을 문자형을 바꿔 리턴
     * @param d 바꾸고자하는 날짜
     * @param format 바꾸고자 하는 포멧
     * @return String 날짜
     */
    public static String dateToString(java.util.Date d, String format)
    {
    	String ch = null;
    	try
    	{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            ch = sdf.format(d);
    	} catch(Exception dfdf) {
    		dfdf.printStackTrace();
    	}
    	return ch;
    }
	/**
	 * description: String값을 float로 변경후 리턴.
	 * @return float parseFloat
	 * @throws Exception
	 */	
    public static float parseFloat(String str){
    	float parseFloat = 0.0f;
    	try{
    		parseFloat = Float.parseFloat(str);
    	}catch(Exception nf){
    		nf.printStackTrace();
    	}
    	return parseFloat;
    }
	/**
	 * description: map을 출력.
	 * @return 
	 * @throws Exception
	 */	
    public static void printHashMap(PrintStream out,HashMap map){
    	Set keyset=map.keySet();
    	String[] keys=(String[])keyset.toArray(new String[0]);
    	for(int i=0;i<keys.length;i++){
    		out.println("[param put] "+keys[i]+" : "+(String)map.get(keys[i]));
    	}
    }
	/**
	 * description: random 키 생성.
	 * @return String identifier
	 * @throws Exception
	 */	
	public static String makeIdentifier(){
		Random ra = new Random(System.currentTimeMillis());
		String identifier = "Identifier-";
		String arraystr[]= {"1","2","3","4","5","6","7","8","9","0","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		for(int i = 0 ; i < 36; i++){
			if(i == 8 || i == 13 || i==18 || i==23){
				identifier = identifier + "-";
			}else{
				identifier = identifier + arraystr[(ra.nextInt(arraystr.length))];
			}
		}
		return identifier;
	}    
	/**
	 * description: 문자형을 날짜로 변경후 리턴.
	 * @return Date ch
	 * @throws Exception
	 */	
	public static java.util.Date stringToDate(String d, String format)
	{
		java.util.Date ch = null;
		try
		{
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        ch = sdf.parse(d);
		} catch(Exception dfdf) {
			dfdf.printStackTrace();
		}
		return ch;
	}	
	/**
	 * description: random 키 생성.
	 * @return String ret
	 * @throws Exception
	 */	
	public static String getRandomKey(int len){
		String ret="";
		for(int i=0;i<len;i++){
			ret+=chararray.charAt(random.nextInt(chararray.length()));
		}
		return ret;
	}	
	/**
	 * description: null 데이터를 제외후 리턴.
	 * @return String 
	 * @throws Exception
	 */	
	public static String truncateNicely(String s, int len, String tail) {
		if (s == null)
	    	return null;
	    
		int srcLen = realLength(s);
		if (srcLen < len)
			return s; 
	
		String tmpTail = tail;
		if (tail == null)
			tmpTail = "";
	 
		int tailLen = realLength(tmpTail);
		if (tailLen > len)
			return ""; 
	
		char a;
		int i = 0;
		int realLen = 0;
		for (i = 0; i < len - tailLen && realLen < len - tailLen; i++) {
			a = s.charAt(i);
			if ((a & 0xFF00) == 0)
				realLen += 1;
			else
				realLen += 2;
			} 
	
			while (realLength(s.substring(0, i)) > len - tailLen) {
			    i--;
			}
			
			return s.substring(0, i) + tmpTail;
	}
	/**
	 * description: 문자 길이 체크후 리턴.
	 * @return int length
	 * @throws Exception
	 */	
	public static int realLength(String s) {
		return s.getBytes().length;
	}	
	
    
	/**
	 * String 문자열을 받아 천단위 구분 기호를 처리한다.
	 * @param str
	 * @return
	 */
	public static String makeComma(String str) {

		StringBuffer sb = new StringBuffer();
		
		int str_size = str.length();       // 받은 문자열의 길이
		int bgn_size = str_size % 3;       // 콤마를 찍기전 자리수

		if ( bgn_size != 0 ) sb.append(str.substring(0,bgn_size));
		if ( ( str_size % 3 != 0 ) && str_size > 3 ) sb.append(",");

		for ( int i=0 ; i<(str_size-bgn_size)/3 ; i++ ) {
			
			sb.append(str.substring(bgn_size+i*3,bgn_size+i*3+3));
			if ( i < (str_size-bgn_size)/3-1 ) sb.append(",");
		}

		return sb.toString();
	}	
	/**
	 * description: 시간데이터를 00시00분00초로 변경후 리턴.
	 * @return String rets
	 * @throws Exception
	 */	
	public static String toScormTimeConvert(String time){
		String rets = "";
		String h = time.substring(2, time.indexOf("H"));
		String m = time.substring(time.indexOf("H")+1, time.indexOf("M"));
		String s = time.substring(time.indexOf("M")+1, time.indexOf("S"));
		if(!h.equals("0")){
			rets = h+"시간";
		}
		if(!m.equals("0")){
			rets += m+"분";
		}
		if(!s.equals("0")){
			rets += s+"초";
		}
		
		return rets;
	}
	/**
	 * description: 시간데이터를 00H00M00S로 변경후 리턴.
	 * @return String rets
	 * @throws Exception
	 */		
	public static int getStudyTime(String time){
		try{
			int rets = 0;		
			int h = Integer.parseInt(time.substring(2, time.indexOf("H")));
			int m = Integer.parseInt(time.substring(time.indexOf("H")+1, time.indexOf("M")));
			int s = Integer.parseInt(time.substring(time.indexOf("M")+1, time.indexOf("S")));
	
			rets = h*3600+m*60+s;
			
			return rets;	
		}catch(Exception e){
			return 0;
		}
	}
}
