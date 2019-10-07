// **********************************************************
//  1. 제      목: 엑셀출력 타입 정의
//  2. 프로그램명 : ExcelPrint.java
//  3. 개      요: 엑셀출력 타입 정의
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 2007. 7. 10
//  7. 수      정:
// **********************************************************

package com.ziaan.common;

import javax.servlet.http.HttpServletResponse;

public class ExcelPrint {
    public static void getCode(HttpServletResponse response, String realFileName) throws Exception { 
	
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
		response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
	}
}