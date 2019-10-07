<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>

<style>
table.ex1 {width:96%; margin:0 auto; text-align:left; border-collapse:collapse}
 .ex1 th, .ex1 td {padding:5px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;font-size:10pt;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>


<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>
</head> 

<body>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<div class="myheader">
				<p class="mytit">시험문제 미리보기</p>
			</div>
			
			
			<!-- contents -->
			<div class="mycon">
				
				
				<form name="<c:out value="${gsMainForm}"/>" method="post">


				
<!--	public final static String OBJECT_QUESTION  = "1";      // 객관식-->
<!--    public final static String SUBJECT_QUESTION = "2";      // 주관식-->
<!--    public final static String OX_QUESTION      = "3";      // OX식 ( add 2005.8.20 )-->
<!--    public final static String MULTI_QUESTION   = "4";      // 다답식-->
				<!-- right -->
				<div class="rightcon">
				<c:set var="temp_examnum" value=""/>
				<c:set var="idx" value="0"/>
				<c:set var="examnum" value="0"/>
				<c:forEach items="${PaperQuestionExampleList}" var="result">
					<c:if test="${result.examnum ne temp_examnum}">
					
						<c:if test="${idx > 0}">
								</ul>
								</fieldset>
							</div>
						</div>
						</c:if>
						
						
						<c:set var="examnum" value="${examnum+1}"/>
						<c:set var="temp_examnum" value="${result.examnum}"/>
						<c:set var="idx" value="${idx+1}"/>		
						<c:set var="titletxt" value=""/>
						
						
						<input type="hidden" name="p_answer" id="p_answer">
						<p style="height:10px;"></p>
						<div class="conbox">
							<p class="que1"><B>문제 ${idx}.</B></p>
							<p class="que2">
							<c:set var="titletxt" value="${fn:replace(result.examtext, lf, '<br/>')}"></c:set>
							<c:if test="${not empty titletxt}">
								<c:if test="${fn:indexOf(titletxt, '보기>') > -1}">
									<c:set var='titletxt'>${fn:replace(titletxt, "보기>", "<table class='ex1'><tr><th scope='col'>보기</th><td>")}</c:set>
									<c:set var="titletxt">${titletxt}</td></tr></table><br/></c:set>
								</c:if>
								
								${titletxt}
							</c:if>
							</p>
							<div class="item">
								<fieldset>
									<legend>선택항목</legend>
									<ul class="checklist">
					</c:if>
					
					
					
					<c:choose>
						<c:when test="${result.examtype eq '1'}">
									<li><input type="radio" class="chr" name="${result.examnum}" value="${result.selnum}" style="cursor:pointer" /> ${result.selnum}) ${result.seltext}</li>
						</c:when>
						<c:when test="${result.examtype eq '2'}">
									<li><textarea name="${result.examnum}" rows="" cols="" class="contents" ></textarea></li>
						</c:when>
						<c:when test="${result.examtype eq '3'}">
									<li><input type="radio" class="chr" name="${result.examnum}" value="${result.selnum}" style="cursor:pointer" /> ${result.selnum}) ${result.seltext}</li>
						</c:when>
						<c:otherwise>
									<li><input type="radio" class="chr" name="${result.examnum}" value="${result.selnum}" style="cursor:pointer"/> ${result.selnum}) ${result.seltext}</li>
						</c:otherwise>
					</c:choose>
		
				</c:forEach>
		
							</ul>
							</fieldset>
						</div>
					</div>
				
				</div>
				<ul class="btnR MT10">
					<li><a class="btn02" href="#" onClick="window.close()"><span>닫기</span></a></li>
				</ul>
				<!-- // right -->
						
				
            
				</form>
		  	</div>
			<!-- //contents -->
		</div>
</body>
</html>
<script type="text/javascript">

	
</script>
