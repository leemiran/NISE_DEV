<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>

<style>
table.ex1 {width:97%; margin:0 auto; text-align:left; border-collapse:collapse}
 .ex1 th, .ex1 td {padding:5px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>



</head> 

<body onload="javascript:begintimer();" onbeforeunload="exit()" onContextmenu="return false">
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2" style="height:690px; overflow: auto;">
		<div class="popCon">
			<!-- header -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				
				<p class="contit">
					시험기간: ${dataMap.startdt} ~ ${dataMap.enddt} / 시험시간: ${dataMap.examtime}분
				</p>
				<p class="contit">
					[시험] ${ExamPaperData.papernum}번째 / [문제수] <c:out value="${ExamPaperData.examcnt}"/>문항
				</p>
				
				<!-- s: 시간제어  -->		
				<b><div align="center" id="numberCountdown"></div></b>
				<!-- e: 시간제어  -->
				
				<form name="<c:out value="${gsMainForm}"/>" method="post">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<input type="hidden" name="p_lesson" 	id="p_lesson" 		value="${p_lesson}"/>
				<input type="hidden" name="p_examtype" 	id="p_examtype" 	value="${p_examtype}"/>
				<input type="hidden" 	 name="p_exam" 		id="p_exam" 	value="${p_exam}"/>
				<input type="hidden" 	 name="answer" 		id="answer" 	value=""/>
				<input type="hidden" name="p_papernum" 	id="p_papernum" 	value="${p_papernum}"/>
				<input type="hidden" name="p_exampoint" 	id="p_exampoint" 	value="${ExamPaperData.exampoint}"/>
				<input type="hidden" name="p_examcnt" 		id="p_examcnt" 		value="${ExamPaperData.examcnt}"/>
				<!-- 재응시 테이블명 추가 -->
				<input type="hidden" 	 name="examtabletemp" 		id="examtabletemp" 	value="${examtabletemp}"/>
				<input type="hidden" name="p_exam_subj"				value="${ExamPaperData.examsubj}"/>

				
<!--	public final static String OBJECT_QUESTION  = "1";      // 객관식-->
<!--    public final static String SUBJECT_QUESTION = "2";      // 주관식-->
<!--    public final static String OX_QUESTION      = "3";      // OX식 ( add 2005.8.20 )-->
<!--    public final static String MULTI_QUESTION   = "4";      // 다답식-->
				<!-- right -->
				<div class="rightcon" style="overflow: auto; height: 400px;">
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
						
						
						<input type="hidden" name="p_answer" id="p_answer" value="${fn:split(answer, ',')[result.rn - 1]}"/>
						<p style="height:10px;"></p>
						<div class="conbox">
							<p class="que1"><b>문제 ${idx}.</b></p>
							<p class="que2">
							
							<c:set var="titletxt" value="${fn:replace(result.examtext, lf, '<br/>')}"></c:set>
							<c:if test="${not empty titletxt}">
								<c:if test="${fn:indexOf(titletxt, '보기>') > -1}">
									<c:set var='titletxt'>${fn:replace(titletxt, "보기>", "<table class='ex1'><tr><th scope='col'>보기</th><td>")}</c:set>
									<c:set var="titletxt">${titletxt}</td></tr></table></c:set>
								</c:if>
								
								${titletxt}
							</c:if>
							
							
							</p>
							<div class="item" style="padding-top:10px;">
								<fieldset>
									<legend>선택항목</legend>
									<ul class="checklist">
					</c:if>
					
					
					
					<c:choose>
						<c:when test="${result.examtype eq '1'}">
									<li><input type="radio" <c:if test="${result.myAnswer eq 'Y'}">checked</c:if> 
									class="chr" name="${result.examnum}" title="${result.examnum}" value="${result.selnum}" style="cursor:pointer" onclick="insertQuestionAnswer(this.value, '${examnum}')" title="${result.seltext}"/> ${result.selnum}) ${result.seltext}</li>
						</c:when>
						<c:when test="${result.examtype eq '2'}">
									<li><textarea name="${result.examnum}" title="${result.examnum}" rows="" cols="" class="contents" onblur="javascript:insertQuestionAnswer(this.value, '${examnum}')" >${result.myAnswer}</textarea></li>
						</c:when>
						<c:when test="${result.examtype eq '3'}">
									<li><input type="radio" <c:if test="${result.myAnswer eq 'Y'}">checked</c:if>
									class="chr" name="${result.examnum}" title="${result.examnum}" value="${result.selnum}" style="cursor:pointer" onclick="insertQuestionAnswer(this.value, '${examnum}')" title="${result.seltext}"/> ${result.selnum}) ${result.seltext}</li>
						</c:when>
						<c:otherwise>
									<li><input type="radio" <c:if test="${result.myAnswer eq 'Y'}">checked</c:if>
									class="chr" name="${result.examnum}" title="${result.examnum}" value="${result.selnum}" style="cursor:pointer" onclick="insertQuestionAnswer(this.value, '${examnum}')" title="${result.seltext}"/> ${result.selnum}) ${result.seltext}</li>
						</c:otherwise>
					</c:choose>
		
				</c:forEach>
		
							</ul>
							</fieldset>
						</div>
					</div>
				
				</div>
				<ul class="btnR MT10">
					<li><a class="btn02" href="#" onclick="whenSubmit()"><span>제출</span></a></li>
				</ul>
				<!-- // right -->
						
				
            
				</form>
		  	</div>
			<!-- //contents -->
		</div>
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

	function insertQuestionAnswer( val, examnum ){

		setAnswer(val, examnum);

		dropStatus = false;
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var url = "<c:out value="${gsDomainContext}" />/com/aja/std/examQuestionAnswerInsert.do";
		var param = "?";

		param += "p_subj=" 		+ frm.p_subj.value;
		param += "&p_year=" 	+ frm.p_year.value;
		param += "&p_subjseq=" 	+ frm.p_subjseq.value;
		param += "&p_lesson=" 	+ frm.p_lesson.value;
		param += "&p_examtype=" + frm.p_examtype.value;
		param += "&p_papernum=" + frm.p_papernum.value;
		param += "&examtabletemp=" + frm.examtabletemp.value;
		param += "&p_examcnt=" 	+ '<c:out value="${ExamPaperData.examcnt}"/>';
		param += "&p_userid=" 	+ '<c:out value="${sessionScope.userid}"/>';
		param += "&p_answer=" 	+ val;
		param += "&p_qNumber=" 	+ examnum;
		
		goGetURL(url, param);

	}

	function setAnswer(val, num){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var target = frm.p_answer;
		for( i=0; i<target.length; i++ ){
			if( num-1 == i ){
				target[i].value = val;
			}
		}
	}

	// 시간을 설정 하세요
	var limit= '<c:out value="${extra_time}"/>'+":01"; // 1분 30초
	var curmin;
	var cursec;
	var parselimit;
	if (document.images) {
		var parselimit=limit.split(":");
		parselimit=parselimit[0]*60+parselimit[1]*1;
	}

	function begintimer(){
		if (!document.images)
			return;

		if (parselimit <= 0 ) {
			whenAutoSubmit();	//시간이 끝나면 자동 제출되어진다.
		} else { 
			parselimit-=1;
			curmin=Math.floor(parselimit/60);
			cursec=parselimit%60;
			
			if (curmin!=0)
				curtime="평가 종료까지 "+curmin+" 분 "+cursec+" 초 가 남았습니다";
			else
				curtime="평가 종료까지 "+cursec+" 초가 남았습니다";

			numberCountdown.innerText=curtime;
			setTimeout("begintimer()",1000);
		}
	}

	function whenSubmit(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var target = frm.p_answer;
		var v_answer = "";
		for( i=0; i<target.length; i++ ){
			if( target[i].value == "" || target[i].value == "0" ){
				alert("응답하지 않은 문제가 있습니다.모든 문제에 응답해주시길 바랍니다.");
				$('.conbox').eq(i).find('input:eq(0), textarea').focus();
				return;
			}
			v_answer += target[i].value + ",";
		}
		
		if(confirm("제출하시겠습니까?")) {
			frm.answer.value = v_answer;
			frm.action = "/usr/stu/std/userStudyExamAnswerFinish.do";
			frm.target = "_self";
			frm.submit();
		}
		
	}

	function whenAutoSubmit(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		//lsy추가
		//자동제출 정답이 들어가지 않는다. 
		var target = frm.p_answer;
		var v_answer = "";
		for( i=0; i<target.length; i++ ){
			v_answer += target[i].value + ",";
		}
		//lsy추가끝
		alert("시험시간이 종료되었습니다. 자동 제출됩니다.");
		frm.answer.value = v_answer;
		frm.action = "/usr/stu/std/userStudyExamAnswerFinish.do";
		frm.target = "_self";
		frm.submit();
	}

	// Ctrl, Alt 키 막기  시작 ///////////////////////////////////////////////////////
	function capturekey(){
		var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();
		if(event.keyCode == 17 ){
			alert("Ctrl 키를 사용하실수 없습니다.");
			event.returnValue=false;
		}
		if(event.keyCode == 18 ){
			alert("Alt 키를 사용하실수 없습니다.");
			event.returnValue=false;
		}
		if(event.keyCode == 116 ){
			event.returnValue=false;
		}
	}
	document.onkeydown = capturekey;
	// Ctrl, Alt 키 막기 끝 //////////////////////////////////////////////////////////

	function exit(){

		if(event.clientY < 250) {

		event.returnValue = "["+curmin+"분] 안에 [답안제출]을 클릭하지 않을 시 현재 체크한 답안으로 자동 제출됩니다!";

		}

	}
	document.title="시험응시 : 나의강의실 : 국립특수교육원부설원격교육연수원";
</script>
