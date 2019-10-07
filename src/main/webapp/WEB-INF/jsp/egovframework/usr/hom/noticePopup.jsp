<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><c:out value="${view.title}"/></title>
	<link rel="stylesheet" type="text/css" href="/css/adm/import.css" />    


<script type="text/javascript">
function notice_setCookie( name, value, expiredays )
{
    var todayDate = new Date();
    todayDate.setDate( todayDate.getDate() + expiredays );
    document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}

function notice_closeWin() 
{ 
	notice_setCookie( "popUpPreview${view.seq}", "done" , 1); // 1=하룻동안 공지창 열지 않음
	self.close(); 
}
</script>
</head>


<body style="background:none;">

<c:if test="${view.temType eq 'A'}">
<!-- 팝업사이즈 473*387 A-->
		<table width="473" height="387" background="/images/adm/common/notice1_bg.jpg">
              <tr>
                   <td valign="top">   
					   <div style="padding:110px 90px 0 100px; height:220px;  font-size:12px;">
							<c:out value="${view.adcontent}" escapeXml="false"></c:out>
					   </div>
					   <div style="padding:20px 40px 0 90px; text-align:right; font-size:12px;">
							오늘 하루 창 닫기 <input name="" type="checkbox" value="" class=" vrM borderNone" onclick="javascript:notice_closeWin()"/>
					   </div>
                   </td>
              </tr>
        </table>
</c:if>


<c:if test="${empty view.temType || view.temType eq 'B'}">
<!-- 팝업사이즈 600*500(세로가변) B -->
		<table width="600" background="/images/adm/common/notice2_bg.gif">
               <tr>
                   <td valign="top" height="131"> 
                   		<img src="/images/adm/common/notice2_top.gif" />
                   </td>
               </tr>
               <tr>
                   <td valign="top" background="/images/adm/common/notice2_center.gif" style="padding:0px 50px 10px; 50px; color:#fff;">
                   		<c:out value="${view.adcontent}" escapeXml="false"></c:out>
                   </td>
               </tr>
               <tr>
                   <td valign="top"> 
                   		<img src="/images/adm/common/notice2_bottom.gif" />
                   </td>
               </tr>
               <tr>
                   <td valign="top"> 
					   <div style="padding:10px 40px 10px 90px; text-align:right; font-size:12px;">
							오늘 하루 창 닫기 <input name="" type="checkbox" value="" class=" vrM borderNone" onclick="javascript:notice_closeWin()"/>
					   </div>
					   </td>
              </tr>
        </table>
</c:if>


<!-- 팝업사이즈 560*696 C-->
<c:if test="${view.temType eq 'C'}">
		<table width="560" height="696" background="/images/adm/common/notice3_bg.jpg">
              <tr>
                   <td valign="top">   
					   <div style="padding:120px 135px 15px 115px; height:520px;  font-size:12px;">
							<c:out value="${view.adcontent}" escapeXml="false"></c:out>
					   </div>
					   <div style="padding:10px 60px 0 130px; text-align:right; font-size:12px;">
							오늘 하루 창 닫기 <input name="" type="checkbox" value="" class=" vrM borderNone" onclick="javascript:notice_closeWin()"/>
					   </div>
                   </td>
              </tr>
        </table>
</c:if>
        
</body>
</html>



