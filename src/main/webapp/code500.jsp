<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>국립특수교육원부설 원격교육연수원</title>
	<link rel="stylesheet" type="text/css" href="/css/adm/import.css" />
<script language="javascript">
function fncGoAfterErrorPage(){
    history.back(-2);
}
</script>	
</head>

<body style="background:none;">

		<table width="100%" height="100%">
              <tr>
                   <td align="center">
                   		<div class="error_500">
                        	<p class="btn_pos"><a href="#none" onClick="fncGoAfterErrorPage()"><img src="/images/adm/common/btn_error.gif" alt="전페이지" /></a><p>
                        </div>
                   </td>
              </tr>
              <tr>
              	<td><!-- 
              		<textarea style="width:100%;height:500px;display:none;">
              		<c:out value="${requestScope['javax.servlet.error.message']}"></c:out>
              		</textarea> -->
              	</td>
              </tr>
        </table>
</body>
</html>
