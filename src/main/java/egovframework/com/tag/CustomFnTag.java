/* 
 * ReportTag.java		1.00	2010-01-21
 *
 * Copyright (c) 2008 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of David.Kim(KJS).  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with David.Kim(KJS).
 */
package egovframework.com.tag;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import egovframework.com.utl.sim.service.EgovFileScrty;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : CustomFnTag.java
 * description : 
 * </pre>
 * 
 * @version
 * 
 * <pre>
 *  
 * 1.0	2010-10-21 
 * 1.1	
 * </pre>
 */

public class CustomFnTag {
	    public static int floor(String input) {
	     Double dblTemp = new Double(input);
	        return (int)Math.floor(dblTemp.doubleValue());
	    }

	    public static int ceil(String input) {
	        Double dblTemp = new Double(input);
	        return (int)Math.ceil(dblTemp.doubleValue());
	    }

	    public static int round(String input) {
	        Double dblTemp = new Double(input);
	        return (int)Math.round(dblTemp.doubleValue());
	    }
	    
	    public static String egovFileScrtyEncoder(String input){
	    	String strEncoder = "";
	    	try {
	    		strEncoder = EgovFileScrty.encode(input);
			} catch (Exception e) {
				e.printStackTrace();
			}	    	
	    	return strEncoder;
	    }
	    
	    public static String toScormTimeConvert(String time){
			String rets = "";
			if( !time.equals("") ){
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
			}else{
				rets = "0초";
			}
			
			return rets;
		}
	    
	    /*
	     * 1자리의 문자이면 01로 변환하여 돌려줌
	     * 없을시는 00
	     * 시간이나 년월일 등에 사용
	     */
	    public static String getNumberToString(String s){
			String rets = "00";
			if( s != null && !s.equals("") ){
				try
				{
					int i = Integer.parseInt(s);
					if(i < 10)
						rets = "0" + s;
					else
						rets = s;
				} catch(Exception e)
				{
					e.getMessage();
					rets = "00";
				}
			}
			
			return rets;
		}
		
		public static String toStudyTimeConvert(String sec){
	    	String resultStr = "";
	    	int second = Integer.valueOf(sec);
			int h = second/3600;
			int m = (second-(h*3600))/60;
			int s = second - (second/60)*60;
			if( h > 0 ) resultStr = h+"시간";
			if( m > 0 ) resultStr += m+"분";
			if( s > 0 ) resultStr += s+"초";
			return resultStr;
	    }
	    
	    public static String titleConvert(String title){
	    	String result = title;
	    	int len = 25;
	    	if( title.length() > len ){
	    		result = title.substring(0,len)+"...";
	    	}
	    	
	    	return result;
	    }
	    
	    public static int toNumber(String str){
	    	int num = 0;
	    	if( !str.equals("") ){
	    		num = Integer.valueOf(str);
	    	}
	    	return num;
	    }
	    
	    public static String getFormatDate(String date, String parsePatterns){
	    	String result = "";
	    	if( date != null && !date.equals("") ){
	    		Date dt = null;
	    		Date compareDate = null;
	    		String dateFormat = "yyyyMMddHHmmss";
	    		dateFormat = dateFormat.substring(0, date.length());
	    		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
	    		try{
	    			dt = df.parse(date);
	    		}catch(ParseException e){
	    			e.printStackTrace();
	    		}
	    		
	    		
	    		Calendar todayDateCal = Calendar.getInstance();
	    		
	    		
	    		todayDateCal.setTime(dt);
	    		
	    		DateFormat datef = new SimpleDateFormat(parsePatterns);
	    		result = DateFormatUtils.format(todayDateCal.getTimeInMillis(), parsePatterns);
	    	}
	    	return result;
	    }
	    
	    public static String getFormatDateNow(String parsePatterns){
	    	String result = "";
	    	result = new SimpleDateFormat(parsePatterns).format(new Date());
	    	return result;
	    }

	    /**
	     *년,월,일,시,분등과 관련된 HTML <option > 을 출력한다.
	     *@param start  시작시간
	     *@param end  종료시간
	     *@param nDefault  default 값이 선택됨
	     *@return HTML <option > 을 출력
	     */
	    public static String getDateOptions(String startTime, String endTime, String nDefaultTime){
	    	String result = "";
	    	int start = 0;
	    	int end = 0;
	    	int nDefault = 0;
	    	
	    	if(startTime != null && !startTime.equals(""))
	    		start = Integer.parseInt(startTime);
	    	
	    	if(endTime != null && !endTime.equals(""))
	    		end = Integer.parseInt(endTime);
	    	
	    	if(nDefaultTime != null && !nDefaultTime.equals(""))
	    		nDefault = Integer.parseInt(nDefaultTime);
	    	
	    	for ( int i=start ; i <= end ; i++ ) { 
	            if ( i < 100 ) { 
	            String temp = "";
	            temp = String.valueOf(i + 100);
	            temp = temp.substring(1);
	            
	                if ( i == nDefault) { 
	                    result += "<option value='" +temp + "' selected > " +temp;
	                }
	                else { 
	                    result += "<option value='" +temp + "' > " +temp;
	                }
	            } else { 
	                if ( i == nDefault) { 
	                    result += "<option value='" +i + "' selected > " +i;
	                }
	                else { 
	                    result += "<option value='" +i + "' > " +i;
	                }
	            }
	        }
	    	
	    	return result;
	    }
	    
	    
	    /**
	     *두 시간의 차이를 분으로 계산한다.  처음 파라메터가 작은 날짜인데 만약 더 큰날짜를 처음으로 주면 음수를리턴.   
	     *예) getMinDifference("20000302","20000303") -- > 3600
	     *      getMinDifference("2000030210","2000030211") -- > 60
	     *      getMinDifference("200003021020","200003021021") -- > 1
	     *      getMinDifference("20000302102000","20000302102130") -- > 1
	     *@return 두시간의 차를 분으로 반환함
	     */
	    public static int getMinDifference(String s_start,String s_end) throws Exception{
	    	long l_gap = getTimeDifference(s_start, s_end);
	        
	        return (int)(l_gap/(1000*60));
	    }
	    public static long getTimeDifference(String s_start,String s_end) throws Exception { 
	        long l_start,l_end,l_gap;
	        
	        int i_start_year=0,i_start_month=1,i_start_day=1,i_start_hour=0,i_start_min=0,i_start_sec=0,i_start_msec=0;
	        int i_end_year=0,i_end_month=1,i_end_day=1,i_end_hour=0,i_end_min=0,i_end_sec=0,i_end_msec=0;
	        
	        try { 
	            try { 
	                i_start_year = Integer.parseInt(s_start.substring(0,4));
	                i_start_month= Integer.parseInt(s_start.substring(4,6)); // month 는 Calendar에서 0 base 으로 작동하므로 1 을 빼준다.
	                i_start_day  = Integer.parseInt(s_start.substring(6,8));
	                i_start_hour = Integer.parseInt(s_start.substring(8,10));
	                i_start_min  = Integer.parseInt(s_start.substring(10,12));
	                i_start_sec  = Integer.parseInt(s_start.substring(12,14));
	                i_start_msec  = Integer.parseInt(s_start.substring(14,17));
	            }
	            catch (IndexOutOfBoundsException ex) { 
	                // ignore
	            }
	            
	            try { 
	                i_end_year = Integer.parseInt(s_end.substring(0,4));
	                i_end_month= Integer.parseInt(s_end.substring(4,6)); // month 는 Calendar 에서0 base 으로 작동하므로 1 을 빼준다.
	                i_end_day  = Integer.parseInt(s_end.substring(6,8));
	                i_end_hour = Integer.parseInt(s_end.substring(8,10));
	                i_end_min  = Integer.parseInt(s_end.substring(10,12));
	                i_end_sec  = Integer.parseInt(s_end.substring(12,14));
	                i_end_msec  = Integer.parseInt(s_end.substring(14,17));
	            }
	            catch (IndexOutOfBoundsException ex) { 
	                // ignore
	            }
	        } catch ( Exception ex ) { 
	            throw new Exception("getFormatDate.getTimeDifference(" +s_start + "," +s_end + ")\r\n" +ex.getMessage() );
	        }
	        
	        Calendar calendar=Calendar.getInstance();
	        
	        calendar.set(i_start_year, i_start_month-1, i_start_day, i_start_hour, i_start_min, i_start_sec);
	        calendar.set(Calendar.MILLISECOND, i_start_msec);

	        l_start=calendar.getTime().getTime();
	        
	        calendar.set(i_end_year, i_end_month-1, i_end_day, i_end_hour, i_end_min, i_end_sec);
	        calendar.set(Calendar.MILLISECOND, i_end_msec);
	        l_end=calendar.getTime().getTime();
	        
	        l_gap=l_end-l_start;
	        
	        return l_gap;
	    }
	     
	    /**
		 * 한글 및 영문 글자 길이를 잘라서 리턴 
		 * @param String 원본문자열
		 * @param int  리턴할 문자열갯수
		 * @return String
		 */
	    public static String getFixTitle(String title, String num) {
			int sublen = 0;
			int len = Integer.valueOf(num);
			StringBuffer sbuf = new StringBuffer();
			if(title == null)
				return "";
			  
			for(int j=0;( j<title.length()&&sublen< len );j++){
		           if(Character.getType(title.charAt(j)) == 5) 
		        	   sublen = sublen+2;//한글
		           else sublen++;     //기타 영문,특수문자,공백
		           
		           sbuf.append(title.charAt(j));
			}
			if(title.length() > len)
				sbuf.append("...");
			
			return sbuf.toString();
		}

	    /**
	     * a/b*100 + %
	     * @param a
	     * @param b
	     * @return
	     */
	     public static String toPercent(Object numerator, Object denominator) {
	    	 String retStr = "";
	    	 try {
	    		 BigDecimal bigNumerator = new BigDecimal(String.valueOf(numerator));
	    		 BigDecimal bigDenominator = new BigDecimal(String.valueOf(denominator));
	    		 
	    		 BigDecimal zero = new BigDecimal(0);
	    		 if(bigDenominator.compareTo(zero) == 1 ) {
	    			 retStr = String.valueOf(bigNumerator.multiply(new BigDecimal(100)).divide(bigDenominator, 2, BigDecimal.ROUND_HALF_UP)) + '%';
	    		 } else {
	    			 retStr = String.valueOf(zero) + '%';
	    		 }
			} catch (Exception e) {
				//logger.error(e.toString());
			}
	    	 return retStr;
	     }
}
