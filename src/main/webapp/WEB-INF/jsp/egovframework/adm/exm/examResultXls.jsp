<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<% 
	String realFileName = "평가결과분석"; 
	//response.reset();
	response.setHeader("Pragma", "no-cache;");
	response.setHeader("Expires", "-1;");
	response.setHeader("Content-Transfer-Encoding", "binary;");
	response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
	response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
	//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");
%>
<html>
<head>
<title>평가결과분석</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
.xl26
	{mso-style-parent:style0;
	color:white;
	font-weight:700;
	text-align:center;
	border:.5pt solid black;
	background:#C2C2C2;
	mso-pattern:auto none;
	white-space:normal;}
.graph { /* 그래프 이미지 */
        height: 14px;
	    border-style: none;
	    background-color: orange;
	    background-repeat: repeat-x;
         }  	
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">


		 <table summary="" cellspacing="0" border="1" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="3%"/>		      
		      <col width="7%"/>
		      <col width="5%"/>
		      <col width="%"/>
		      <col width="%"/>
              <col width="10%"/>
		      <col width="5%"/>
		      <col width="5%"/>
		      <col width="8%"/>
              <col width="8%"/>
              <col width="8%"/>
              <col width="8%"/>
              <col width="5%"/>
              <col width="5%"/>
              <col width="%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row" rowspan="2">No</th>
		        <th scope="row" rowspan="2">ID(재채점)</th>
		        <th scope="row" rowspan="2">성명</th>
		        <th scope="row" rowspan="2">전화번호</th>
		        <th scope="row" rowspan="2">휴대전화</th>
		        <th scope="row" rowspan="2">이메일주소</th>
		        <th scope="row" colspan="2">상태(평가보기)</th>
		        <th scope="row" colspan="2">최초응시일시</th>
          		<th scope="row" colspan="2">완료일시</th>
          		<th scope="row" colspan="2">평가점수</th>
          		<th scope="row" rowspan="2">성적반영점수</th>
          		<th scope="row" rowspan="2">사용자<br/>응시횟수<br/>(재응시셋)</th>
	          </tr>
	          
	          <tr>
		        <th scope="row">1차</th>
		        <th scope="row">2차</th>
		       <th scope="row">1차</th>
		        <th scope="row">2차</th>
		        <th scope="row">1차</th>
		        <th scope="row">2차</th>
		        <th scope="row">1차</th>
		        <th scope="row">2차</th>
	          </tr>
	          
	        </thead>
		    <tbody>
		    
<c:forEach items="${list}" var="result" varStatus="status">
		      <tr>
		        <td class="num">${status.count}</td>
		        <td>${result.userid}</td>
		        <td>${result.name}</td>
		        <td style='mso-number-format:\@;'>${result.hometel}</td>
		        <td style='mso-number-format:\@;'>${result.handphone}</td>
		        <td>${result.email}</td>
		        <td>
		        	<c:choose>
		        		<c:when test="${not empty result.started && empty result.ended}">
			        		
			        			<font color="green">응시(미제출)</font>
			        			
		        		</c:when>
		        		<c:when test="${empty result.answercnt}">
		        			<font color="red">미응시</font>
		        		</c:when>
		        		<c:otherwise>
			        		
			        			<font color="blue">완료</font>
			        		
		        		</c:otherwise>
		        	</c:choose>
		        </td>
		     	<td>
		        	<c:choose>
		        		<c:when test="${not empty result.startedTemp && empty result.endedTemp}">
			        		
			        			<font color="green">응시(미제출)</font>
			        			
		        		</c:when>
		        		<c:when test="${empty result.answercntTemp}">
		        			<font color="red">미응시</font>
		        		</c:when>
		        		<c:otherwise>
		        			
		        				<font color="blue">완료</font>
		        			
		        		</c:otherwise>
		        	</c:choose>
		        </td>
		        <td><c:out value="${fn2:getFormatDate(result.indate, 'yyyy.MM.dd HH:mm')}"/></td>
		        <td><c:out value="${fn2:getFormatDate(result.indateTemp, 'yyyy.MM.dd HH:mm')}"/></td>
		        
		        <td><c:out value="${fn2:getFormatDate(result.ended, 'yyyy.MM.dd HH:mm')}"/></td>
		        <td><c:out value="${fn2:getFormatDate(result.endedTemp, 'yyyy.MM.dd HH:mm')}"/></td>
		        <td>${result.score}</td>
		        <td>${result.scoreTemp}</td>
		        <td>${result.ftest}(${result.avftest})</td>
		        <td>
		        <c:if test="${empty result.answercnt}">
		        	${result.userretry}
			        (${result.retrycnt})
		        </c:if>
		        <c:if test="${not empty result.answercnt}">
		        
			        ${result.userretry}
			        (${result.retrycnt})
		        
		        </c:if>
		        </td>
	          </tr>
</c:forEach>
	          
	        </tbody>            
	      </table>


</body>
</html>
