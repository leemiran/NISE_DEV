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
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<body>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2" style="height:690px;">
		<div class="popCon">
			<!-- header -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				
				
				<h4 class="tit">시험</h4>
				<!-- search wrap-->
				<form name="<c:out value="${gsMainForm}"/>" method="post">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<input type="hidden" name="p_seq" id="p_seq" />
				<input type="hidden" name="p_tabseq" id="p_tabseq" />
				<input type="hidden" name="p_lesson" id="p_lesson" />
				<input type="hidden" name="p_examtype" id="p_examtype" />
				<input type="hidden" name="p_papernum" id="p_papernum" />
				<input type="hidden" name="examtabletemp" id="examtabletemp" />
				<input type="hidden" name="p_userid" id="p_userid" value="${sessionScope.userid}"/>
				<input type="hidden" name="p_gyear"				value="${p_year}"/>
				<input type="hidden" name="p_exam_subj"				value=""/>
				
				
				
				</form>
				<!-- // search wrap -->
				<!-- list -->
				<div class="tbList1">
					<table summary="제목, 시험유형, 진행상황, 나의기준, 기간으로 구성" style="width:100%">
						<caption>시험목록</caption>
						<colgroup>
							<col width="" />
							<col width="90px" />
							<col width="90px" />
							<col width="100px" />
							<col width="80px" />
							<col width="150px" />
						</colgroup>
						<thead>
							<tr>
								<th scope="col">제목</th>
								<th scope="col">시험유형</th>
								<th scope="col">진행상황</th>
								<th scope="col">나의기준</th>
								<th scope="col">시험시간</th>
								<th scope="col">시험기간</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${exam_list}" var="result">
								<tr>
									<td class="left">
										<c:choose>
											<c:when test="${result.sulcheck eq 'N'}">
												<a href="#none" onclick="alert('설문 응시를 먼저 진행하세요.');">
													<c:out value="${result.examtype eq 'M' ? '출석평가' : '시험 응시하기'}" />
												</a>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${result.indata eq 'Y'}">														
														<c:choose>															
															<c:when test="${result.submitYn eq 'Y'}">
																<!-- 응시 완료-->
																<c:if test="${result.submitTempYn eq 'Y' && ( empty(result.userretry) || result.userretry == 0) }" >
																	<a href="#none" onclick="alert('이미 응시하셨습니다.');"><c:out value="${result.examtype eq 'M' ? '출석평가' : '시험 응시하기'}" /></a>
																</c:if>
																<!-- 재응시 또는 추가 응시-->	
																<c:if test="${result.submitTempYn eq 'N' || result.userretry > 0}">
																	<a href="#none" onclick="popupExamAgree('${result.lesson}', '${result.examtype}', '${result.papernum}', '${result.examsubj }');">
																		<c:out value="${result.examtype eq 'M' ? '출석평가' : '시험 응시하기'}" />
																	</a>
																</c:if>		
															</c:when>															
															<c:otherwise>
																<!-- 미응시-->
																<a href="#none" onclick="popupExamAgree('${result.lesson}', '${result.examtype}', '${result.papernum}', '${result.examsubj }');">
																	<c:out value="${result.examtype eq 'M' ? '출석평가' : '시험 응시하기'}" />
																</a>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<a href="#none" onclick="alert('진도가 90% 이상 되어야 응시할 수 있습니다.');"> <c:out value="${result.examtype eq 'M' ? '출석평가' : '시험 응시하기'}" /></a>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${result.submitYn eq 'Y'}">
											    <c:out value="${result.examtype eq 'M' ? '출석평가' : '온라인평가'}" />
												<a href="javascript:IndividualResult('${result.lesson}','${result.examtype}','${result.papernum}', '')">
													 1차결과
												</a>
												<c:if test="${result.submitTempYn eq 'Y'}">											    
													<a href="javascript:IndividualResult('${result.lesson}','${result.examtype}','${result.papernum}', 'temp')">
														 </br>2차결과
													</a>
												</c:if>
											</c:when>
											
											<c:otherwise>
												<c:out value="${result.examtype eq 'M' ? '출석평가' : '온라인평가'}" />
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${result.indata eq 'Y'}">
												<!-- 제출 -->
												<!-- 미제출 -->
												<c:choose>
													<c:when test="${result.submitYn eq 'Y'}">
														<!-- 재응시 -->
														<!-- 응시 -->
														<c:choose>
															
															<c:when test="${result.submitTempYn eq 'N' || result.userretry > 0}">
																<font color="orange">재응시가능</font>
															</c:when>
															<c:otherwise>
																<font color="orange">응시함</font>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<font color="orange">응시가능</font>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												-
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.started}">
												미응시
											</c:when>
											<c:otherwise>
												<font color="blue">응시</font>
												<c:choose>
													<c:when test="${result.submitYn eq 'Y'}">
														<font color="blue">(제출)</font>
													</c:when>
													<c:otherwise>
														<font color="red">(미제출)</font>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</td>
									<td>${result.extratime}분</td>
									<td>${fn2:getFormatDate(result.startdt, 'yyyy.MM.dd')}~ ${fn2:getFormatDate(result.enddt, 'yyyy.MM.dd')}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				
				<br/>
				<div>
					<strong style="color: red;">※ 온라인평가 유의사항(필독)</strong>
					<ul style="margin-left: 20px;">
						<li>총 2회(60분) 응시가 가능하므로 여유시간을 가지고 응시해 주세요.(2회 초과시 재응시 절대불가)</li> 
						<li>2회 응시 중 최고 점수로 적용되어 반영됩니다.(1회 응시 후 이수가능 점수가 되시면 2회 응시 안하셔도 됩니다.)</li>
						<li>최초 응시시간에서 60분 이내로 응시해야 하며, 평가 응시 중 비정상 종료될시 시간안에 다시 접속하시면 계속해서 응시 가능합니다.</li> 
						<li>평가 시간(60분) 만료 시 자동으로 평가 제출됩니다. </li>
						<li>일부 문제만 풀고 제출 할 경우 체크한 문항만 점수에 반영 됩니다. </li>
						<li>온라인시험 미응시 할 경우 미이수처리 되오니 주의하시기 바랍니다.</li>
						<li>안정적인 응시를 위하여 시험기간 종료일 및 접속 집중 시간대(14:00~16:00)를 피하여 응시하여 주세요.</li> 
						<li>평가 응시 마지막날(연수 마지막날)은 오후5시까지 응시하여주세요.  </li>
					</ul> 
				</div>
				<ul class="btnR">
					<c:if test="${popupClose eq 'Y'}">
					<li><a class="btn02" href="#" onclick="window.close()"><span>닫기</span></a></li>
					</c:if>
				</ul>
            
		  	</div>
			
			<!-- //contents -->
		</div>
	</div>
</div>

<!-- 중복 아이피 경고 팝업 -->
<style type="text/css">
.bgLayer {display: none; position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: #000; opacity: .5; filter: alpha(opacity=50); z-index: 10;}
#warningPopup {background:url(/images/popup_bg.png) 0 0 no-repeat; display: block; width: 500px; height: 300px; z-index: 100; position: absolute; top: 20%; left: 10%; overflow: hidden; border-radius: 3px; box-shadow: 0px 1px 1px #333;}
</style>
<div id="warningPopup" style="position: absolute; visibility: hidden;">
	<form name="popupForm" id ="popupForm">
	<input type="hidden" name="lesson" id="lesson" value="" />
	<input type="hidden" name="examtype" id="examtype" value="" />
	<input type="hidden" name="papernum" id="papernum" value="" />
	<input type="hidden" name="p_exam_subj" id="p_exam_subj" value="" />
	<br>
	<input type="button" id="popupBtnGo" style="position:absolute; left:28px; bottom:31px; width:185px; height:37px; background:url(/images/btn1.png) 0 0 no-repeat; border:0 none; padding:0 none; margin:0 none; cursor:pointer;" />
	<input type="button" id="popupBtnBack" style="position:absolute; right:28px;; bottom:31px; width:242px; height:37px; background:url(/images/btn2.png) 0 0 no-repeat; border:0 none; padding:0 none; margin:0 none; cursor:pointer;" />
	</form>
</div>

</body>
</html>
<script type="text/javascript">
function bgLayerOpen() {
	if (!jQuery('.bgLayer').length) {
		jQuery('<div class="bgLayer"></div>').appendTo(jQuery('body'));
	}
	var w = jQuery(document).width() + 12;
	var h = jQuery(document).height();
	var object = jQuery('.bgLayer');
	object.css({'width': w, 'height': h});
	object.fadeIn(500);
	jQuery('html').css('overflow', 'hidden');
}

function bgLayerClear() {
	var object = jQuery('.bgLayer');
	if (object.length) {
		object.fadeOut(500, function () {
			object.remove();
		});
	}
	jQuery('html').css('overflow', 'scroll');
}

function popupOpen() {
	var layerPopupObj = jQuery('#warningPopup');
	var left = (jQuery(window).scrollLeft() + (jQuery(window).width() - layerPopupObj.width()) /2);
	var top = (jQuery(window).scrollTop() + (jQuery(window).height() - layerPopupObj.height()) /2);
	layerPopupObj.css({'left': left, 'top': top, 'position': 'absolute'});
	jQuery('body').css({'position': 'relative'}).append(layerPopupObj);
	if (document.all.warningPopup.style.visibility == "hidden") {
		bgLayerOpen();
		document.all.warningPopup.style.visibility = "visible";
		return false;
	} else {
		bgLayerClear();
		document.all.warningPopup.style.visibility = "hidden";

		$("form").each(function() {  
			if(this.id == "popupForm") this.reset();  
		});
	}
}

function chkDuplicateIP(cb) {
	jQuery.ajax({
		url: "/usr/stu/std/userStudyCheckDuplicateIP.do",
		success: function(data) {
			if (parseInt(data) == 0) cb(true);
			else cb(false);
		},
		error: function(data) {
			cb(false);
		}
	});
}

/* ********************************************************
 * 시험
 ******************************************************** */
 
function popupExamAgree(lesson, examtype, papernum, p_exam_subj) {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	window.open("","popupExamAgree","width=400,height=305,scrollbars=yes");
	frm.action = "/usr/stu/std/popupExamAgree.do";
	frm.target = "popupExamAgree";
	
	frm.p_lesson.value = lesson;
	frm.p_examtype.value = examtype;
	frm.p_papernum.value = papernum;
	frm.p_exam_subj.value = p_exam_subj;
	frm.submit();
}

function goExamAnswerPage(lesson, examtype, papernum, p_exam_subj) {
	
	chkDuplicateIP(function(rtn) {
		if (rtn) {
			goExamAnswerPageAction(lesson, examtype, papernum, p_exam_subj);
		} else {
			// 접속중인 중복아이피가 있다. 팝업 노출
			jQuery('#lesson').val(lesson);
			jQuery('#examtype').val(examtype);
			jQuery('#papernum').val(papernum);
			jQuery('#p_exam_subj').val(p_exam_subj);
			popupOpen();
		}
	});
}

function goExamAnswerPageAction(lesson, examtype, papernum, p_exam_subj) {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	frm.action = "/usr/stu/std/userStudyExamAnswerPage.do";
	
	/* var msg = "평가는 2회 응시가 가능합니다. (2회 응시가능)\n";
	msg += "평가 2회 응시 점수중 가장 높은 점수를 기준으로 점수가 반영됩니다.\n";
	msg += "한번열었다 닫아도 응시로 되오니(2회 응시가능)\n";
	msg += "시간여유 가지시고 응시하시기 바랍니다."; */
	var msg = "평가는 1회 응시가 가능합니다.\n";
	//msg += "평가 2회 응시 점수중 가장 높은 점수를 기준으로 점수가 반영됩니다.\n";
	msg += "한번열었다 닫아도 응시로 되오니\n";
	msg += "시간여유 가지시고 응시하시기 바랍니다.";
	
	//alert(msg);
	
	//if( confirm("계속 진행하시겠습니까?") ) {
		frm.p_lesson.value = lesson;
		frm.p_examtype.value = examtype;
		frm.p_papernum.value = papernum;
		frm.p_exam_subj.value = p_exam_subj;
		
		frm.target = "_self";
		frm.submit();
	//}
}

/* ********************************************************
* 시험 결과보기
******************************************************** */
function IndividualResult(lesson, examtype, papernum, examtabletemp){
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	window.open("","examUserExamViewPopupWindowPop","width=800,height=600,scrollbars=yes");
	frm.p_lesson.value = lesson;
	frm.p_examtype.value = examtype;
	frm.p_papernum.value = papernum;
	frm.examtabletemp.value = examtabletemp;
	frm.action = "/usr/stu/std/userStudyExamAnswerView.do";
	frm.target = "examUserExamViewPopupWindowPop";
	frm.submit();
}

jQuery(function () {
	jQuery(window).resize(function () {
		jQuery('.bgLayer').css('width', jQuery(window).width() - 0);
		jQuery('.bgLayer').css('height', jQuery(window).height() - 0);
	});

    $("#popupBtnBack").click(function() {
    	popupOpen();
    });
    $("#popupBtnGo").click(function() {
    	var lesson = jQuery('#lesson').val();
		var examtype = jQuery('#examtype').val();
		var papernum = jQuery('#papernum').val();
		var p_exam_subj = jQuery('#p_exam_subj').val();
    	popupOpen();
		goExamAnswerPageAction(lesson, examtype, papernum, p_exam_subj);
    });
});
</script>
