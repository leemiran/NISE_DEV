<%@ page contentType = "text/html;charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import = "java.util.*" %>
<%@ page import = "com.ziaan.common.*" %>
<%@ page import = "com.ziaan.library.*" %>
<%

	String 		v_date 		= FormatDate.getDate("yyyyMMddHHmmss");
	String 		realFileName= "�ù���ڵ�";
	ExcelPrint.getCode(response, realFileName);	 
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">
      <table border="1">
        <tr>
          <td><b>NO</b></td>
          <td  style='mso-number-format:"\@";'><b>�ù���ڵ�</b></td>  
          <td><b>�ù���</b></td>                                                                
        </tr>
       <c:forEach items="${list}" var="result" varStatus="status">
       <tr>
          <td>${status.index + 1 }</td>
          <td style='mso-number-format:"\@";'>${result.code}</td>                          
          <td>${result.codenm}</td>           
        </tr>
        </c:forEach>                  
      </table>         
</body>
</html>