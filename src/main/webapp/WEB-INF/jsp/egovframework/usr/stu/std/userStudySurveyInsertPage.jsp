<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>
</head> 

<body  onContextmenu="return false">
<div id="mystudy2">
	<div class="con">
	<div class="popCon">	
		<!-- header -->
		<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>		
		<!-- //header -->
		<!-- contents -->
		<form name="<c:out value="${gsMainForm}"/>" method="post">
		<input type="hidden" name="p_sulpapernum" id="p_sulpapernum" value="${p_sulpapernum}"/>
		<input type="hidden" name="p_userid" value="${p_userid }"/>		
				
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<p class="contit">
					<c:out value="${p_subjnm}"/><!--  <span class="tred">[필수]</span> -->
				</p>

				<!--    public final static String OBJECT_QUESTION  = "1";  // 단일선택		-->
				<!--    public final static String MULTI_QUESTION   = "2";  // 복수선택     	-->
				<!--    public final static String SUBJECT_QUESTION = "3";	// 서술형			-->
				<!--	public final static String COMPLEX_QUESTION = "4";	// 복합형			-->
				<!--    public final static String FSCALE_QUESTION = "5";	// 5점척도		-->
				<!--    public final static String SSCALE_QUESTION = "6";	// 7점척도		-->

				<div class="conbox" style="height: 400px; overflow: auto">
				<c:set var="sulIdx" value="1"/>
				<c:set var="tempSul" value=""/>
				<c:set var="tempSelCount" value="0"/>
				<c:set var="selnumber" value="0" />
				<c:forEach items="${PaperQuestionExampleList}" var="result"  varStatus="i">
	
					<c:if test="${tempSul ne result.sulnum}">
						<input type="hidden" id="checkVal" name="checkVal"/>
						<input type="hidden" id="checksulnum" name="checksulnum" value="${sulIdx}"/>
						<c:set var="tempSelCount" value="0"/>
						<p class="que1">문제 <c:out value="${sulIdx}"></c:out>. [${result.sultypenm}]</p>
						<p class="que2">${result.sultext}</p>
						<c:set var="sulIdx" value="${sulIdx+1}"/>
						
						<c:set var="selnumber" value="0" />
						
						<div class="item">
							<fieldset>
								<!-- <legend>${result.distcodenm}</legend> -->
								<ul class="checklist">
					</c:if>
					<c:set var="tempSelCount" value="${tempSelCount+1}"/>
					<c:set var="selnumber" value="${selnumber+1}" />
					<c:choose>
						<c:when test="${result.sultype eq '2'}">
							<li><input name="${result.sulnum}" type="checkbox"  class="chr" onclick="setCheckVal(this.value, ${sulIdx-1})" value="${result.selnum}" title="${result.seltext }"/> <%-- ${result.selnum}) --%> ${selnumber}) ${result.seltext }</li>
						</c:when>
						<c:when test="${result.sultype eq '3'}">
							<li><textarea name="${result.sulnum}<c:if test="${result.distcode eq '10'}">|10</c:if>" rows="8" style="width:98%" onblur="javascript:setCheckVal(this.value, ${sulIdx-1})"></textarea></li>
						</c:when>
						<c:when test="${result.sultype eq '4'}">
							<li><input name="${result.sulnum}" type="radio"  class="chr" onclick="setCheckVal(this.value, ${sulIdx-1})" value="${result.selnum}" title="${result.seltext }"/> <%-- ${result.selnum}) --%> ${selnumber}) ${result.seltext }</li>
							<c:if test="${tempSelCount eq result.selcount}">
							<li><textarea name="${result.sulnum}|C" rows="8" style="width:98%"></textarea></li>
							</c:if>
						</c:when>
						<c:otherwise>
							<li><input name="${result.sulnum}" type="radio"  class="chr" onclick="setCheckVal(this.value, ${sulIdx-1})" value="${result.selnum}" title="${result.seltext }"/> <%-- ${result.selnum}) --%> ${selnumber}) ${result.seltext }</li>
						</c:otherwise>
					</c:choose>
					<c:if test="${tempSelCount eq result.selcount || result.selcount == 0 || result.selcount eq null}">
							</ul>
						</fieldset>
					</div>
					</c:if>
					<c:set var="tempSul" value="${result.sulnum}"/>
				</c:forEach>				

				<p class="que2">교육후기</p>
				<c:set var="sulIdx" value="${sulIdx+1}"/>
				<div class="item">
					<fieldset>
						<legend>${result.distcodenm}</legend>
						<ul class="checklist">
							<li><textarea name="p_comments" id="p_comments" rows="8" style="width:98%" maxlength="500" title="교육후기"></textarea></li>
						</ul>
					</fieldset>
					
					<!-- right -->
					<div class="rightcon">
						<ul class="btnR MT10">
							<li><a class="btn02" href="#" onclick="whenSubmit()"><span>제출</span></a></li>
						</ul>
					</div>
					<!-- // right -->
		
				</div>
		  	</div>
				
		
		
		</form>
		</div>
		<!-- //contents -->
	</div>
</div>
</body>
</html>
<script type="text/javascript">

	/* ********************************************************
	 * 조회
	 ******************************************************** */
	function doPageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyDataList.do";
		frm.target = "_self";
		frm.submit();
	}

	function setCheckVal(val, num){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var target = frm.checkVal;
		for( i=0; i<target.length; i++ ){
			if( num-1 == i ){
				target[i].value = val;
			}
		}
	}

	function whenSubmit(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var target = frm.checkVal;
		var checksulnum = frm.checksulnum;		
		for( i=0; i<target.length; i++ ){
			if( target[i].value == "" ){				
				alert(checksulnum[i].value +"번 설문에 응답하지 않으셨습니다. \n모든 설문에 응답해주시길 바랍니다.");								
				return;
			}
		}
		
		frm.action = "/usr/stu/std/userStudySurveyInsertData.do";
		frm.target = "_self";
		frm.submit();
	}
	document.title="설문작성 : 나의 강의실 : 국립특수교육원부설원격교육연수원 ";
</script>
