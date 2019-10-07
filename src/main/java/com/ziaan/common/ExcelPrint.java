// **********************************************************
//  1. ��      ��: ������� Ÿ�� ����
//  2. ���α׷��� : ExcelPrint.java
//  3. ��      ��: ������� Ÿ�� ����
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: 2007. 7. 10
//  7. ��      ��:
// **********************************************************

package com.ziaan.common;

import javax.servlet.http.HttpServletResponse;

public class ExcelPrint {
    public static void getCode(HttpServletResponse response, String realFileName) throws Exception { 
	
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
		response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// �ѱ� ���ϸ� �����ÿ�..
	}
}