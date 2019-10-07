//**********************************************************
//  1. 제      목: DatePicker
//  2. 프로그램명: DatePickerBean.java
//  3. 개      요: DatePicker 구성
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 장동진
//  7. 수      정:
//**********************************************************

package com.ziaan.common;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.ziaan.library.*;

public class SearchFormBean {

    /**
    *  DatePicker
    */
    public static String getDatePicker(String name, String value, String event) throws Exception   {
      String result = "";
    	try{
    		/*
				result += "\n <div id='fDatePicker"+ name +"' style='position:absolute;z-index:10;display:none;'>  							";
				result += "\n <script type='text/javascript'>                                                                    	";
				result += "\n var flashObj = new FlashObject ('/js/datepicker.swf', 'dtpickers_"+ name +"', '210', '220', '#FFCCCC');        ";
				result += "\n flashObj.addVariable ('p_date', '"+ value +"');                                                     ";
				result += "\n flashObj.addVariable ('p_function', '"+ name +"');                                                 ";
				
				result += "\n flashObj.write ('fDatePicker"+ name +"');                                                         ";
				result += "\n </script>                                                                                           ";
				result += "\n </div>                                                                                              ";
				result += "\n <input type='text' name='"+ name +"' id='"+ name +"' value='"+ value +"' size='10' class='input2' onclick='"+ event +"' onblur='chkDate(this)'>&nbsp;<img src='/images/btn/calendar.gif' align='absmiddle' style='cursor:pointer' onclick=\"whenShowDatePicker('fDatePicker"+ name +"', this);\"> ";
				*/
				result = "<span><input type='text' id='" + name + "' name='" + name + "' isEmpty='true' empty='지정안함' isActive='true' arrowImage='/dapa_images/ico_dtp_def.gif' prevImage='/dapa_images/ico_arrow_left_gray.gif' nextImage='/dapa_images/ico_arrow_right_gray.gif' value='" + value + "' " + event + " ><script type='text/javascript'>new xwzDatePicker(document.getElementById('" + name + "') ) ;</script></span>";

	      }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        return result;
    }
    
    public static String getDatePicker2(String name, String value, String event) throws Exception   {
      String result = "";
    	try{
    		/*
			result += "\n <div id='fDatePicker"+ name +"' style='position:absolute;z-index:10;display:none;'>  							";
			result += "\n <script type='text/javascript'>                                                                    	";
			result += "\n var flashObj = new FlashObject ('/js/datepicker.swf', 'dtpickers_"+ name +"', '210', '220');        ";
			result += "\n flashObj.addVariable ('p_date', '"+ value +"');                                                     ";
			result += "\n flashObj.addVariable ('p_function', '"+ name +"');                                                 ";
			result += "\n flashObj.write ('fDatePicker"+ name +"');                                                         ";
			result += "\n </script>                                                                                           ";
			result += "\n </div>                                                                                              ";
			result += "\n <input type='text' name='"+ name +"' id='"+ name +"' value='"+ value +"' size='10' class='input2' "+ event +"   onblur='chkDate(this)'>&nbsp;<img src='/images/btn/calendar.gif' align='absmiddle' style='cursor:pointer' onclick=\"whenShowDatePicker('fDatePicker"+ name +"', this);\"> ";
			*/
			result = "<span> <input type='text' id='" + name + "' name='" + name + "' isEmpty='true' empty='지정안함' arrowImage='/dapa_images/ico_dtp_def.gif' prevImage='/dapa_images/ico_arrow_left_gray.gif' nextImage='/dapa_images/ico_arrow_right_gray.gif' value='" + value + "' " + event + " ></span> <script type='text/javascript'>new xwzDatePicker(document.getElementById('" + name + "') ) ;</script>";


	      }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        return result;
    }

    public static String getAutoSelect(String name, String value, String urls, String size, String param, String relate) throws Exception   {
      String result = "";
    	try{
    		result += "<script language='javascript'>";
			result += "var rhAutoSelect"+ name+" = new rhAutoSelect('"+ name +"', '"+ value +"', '"+ urls +"', '"+ size +"', '"+ relate +"', '"+ param +"', 'false'); ";
			result += "</script>";

      }
      catch (Exception ex) {
          ErrorManager.getErrorStackTrace(ex);
          throw new Exception(ex.getMessage());
      }
      return result;
   }
   
   
    public static String getDatePicker3(String name, String value, String event) throws Exception   {
      String result = "";
    	try{
				result = "<span> <input type='text' id='" + name + "' name='" + name + "' isEmpty='true' empty='' arrowImage='/dapa_images/ico_dtp_def.gif' prevImage='/dapa_images/ico_arrow_left_gray.gif' nextImage='/dapa_images/ico_arrow_right_gray.gif' value='" + value + "' " + event + "></span> <script type='text/javascript'>new xwzDatePicker(document.getElementById('" + name + "') ) ;</script>";

	      }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        return result;
    }
}
