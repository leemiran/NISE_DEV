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
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}\n개인정보 수정 페이지로 이동합니다.");
	opener.changeMenu(3, 5);
	window.close();
</c:if>
-->
</script>

<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>
</head> 

<body>
<form name="<c:out value="${gsMainForm}"/>" method="post">
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
<input type="hidden" name="p_seq" id="p_seq" />
<input type="hidden" name="p_tabseq" id="p_tabseq" />
<input type="hidden" name="p_lesson" id="p_lesson" />
<input type="hidden" name="p_examtype" id="p_examtype" />
<input type="hidden" name="p_papernum" id="p_papernum" />


<input type="hidden" name="p_class" id="p_class" />
<input type="hidden" name="p_ordseq" id="p_ordseq" />
<input type="hidden" name="p_exam_subj"				value=""/>

</form>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2" style="height:690px;">
		<div class="popCon">
			<!-- header -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				<div class="myincon">
					<div class="left">
						<div class="homtab">
							<h4 class="homtit1">공지사항</h4>
							<a href="/usr/stu/std/userStudyNoticeList.do" onclick="changePage('10');return false;"><img src="/images/adm/study/mystudy_btnmore.gif" alt="더보기" /></a>
						</div>
						<ul class="homList" style="height:50px;">
						<c:forEach items="${selectGongList}" var="result">
							<li>
								<a href="#" onclick="noticeView('${result.seq}')">
								${fn2:getFixTitle(result.title, 40)}
								<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.addate, 'yyyyMMdd'), PCurrentDate) == 0}">
									<img src="/images/adm/ico/ico_new.gif" alt="new" />
								</c:if>
								</a>
								<span class="date">${fn2:getFormatDate(result.addate, 'yyyy.MM.dd')}</span>
							</li>
						</c:forEach>
						<c:if test="${empty selectGongList}">
							<li>
								등록된 내용이 없습니다.
							</li>
						</c:if>
						</ul>
					</div>
					<div class="right">
						<div class="homtab">
							<h4 class="homtit1">자료실</h4>
							<a href="/usr/stu/std/userStudyDataList.do" onclick="changePage('11');return false;"><img src="/images/adm/study/mystudy_btnmore.gif" alt="더보기" /></a>
						</div>
						<ul class="homList" style="height:50px;">
						<c:forEach items="${sdList}" var="result">
							<li>
								<a href="#" onclick="dataView('${result.seq}')">
								${fn2:getFixTitle(result.title, 40)}
								<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.ldate, 'yyyyMMdd'), PCurrentDate) == 0}">
									<img src="/images/adm/ico/ico_new.gif" alt="new" />
								</c:if>
								</a>
								<span class="date">${fn2:getFormatDate(result.ldate, 'yyyy.MM.dd')}</span>
							</li>
						</c:forEach>
						<c:if test="${empty sdList}">
							<li>
								등록된 내용이 없습니다.
							</li>
						</c:if>
						</ul>
					</div>
				</div>
				
				<div class="myincon">
					<div class="left">
						<div class="homtab">
							<h4 class="homtit2">과정문의</h4>
							<a href="/usr/stu/std/userStudyQnaList.do" onclick="changePage('12');return false;"><img src="/images/adm/study/mystudy_btnmore.gif" alt="더보기" /></a>
						</div>
						<ul class="homList" style="height:50px;">
						<c:forEach items="${sqList}" var="result">
							<li>
								<c:if test="${result.isopen ne 'Y' and result.userid ne sessionScope.userid and result.fuserid ne sessionScope.userid}">
								<a href="#" onclick="alert('비공개 글입니다.')">${fn2:getFixTitle(result.title, 40)}</a> 
								</c:if>
								<c:if test="${result.isopen eq 'Y' or result.userid eq sessionScope.userid or result.fuserid eq sessionScope.userid}">
								<a href="#" onclick="qnaView('${result.seq}')">${fn2:getFixTitle(result.title, 40)}</a> 
								</c:if>
								<c:if test="${fn2:getMinDifference(fn2:getFormatDate(result.indate2, 'yyyyMMdd'), PCurrentDate) == 0}">
									<img src="/images/adm/ico/ico_new.gif" alt="new" />
								</c:if>
								</a>
								<span class="date">${fn2:getFormatDate(result.ldate, 'yyyy.MM.dd')}</span>
							</li>
						</c:forEach>
						<c:if test="${empty sqList}">
							<li>
								등록된 내용이 없습니다.
							</li>
						</c:if>
						</ul>
					</div>	
                    <div class="right">
						<div class="homtab">
							<h4 class="homtit2">다운로드</h4>							
						</div>
						<ul class="homList">
                            <li><a href="http://get.adobe.com/kr/flashplayer" target="_blank">플래쉬 플레이어 최신버전 다운로드</a></li>							
					  </ul>
					</div>			
				</div>
				
				<!-- list -->
				<div class="tbList1">
					<table summary="평가항목,제목,진행상황,나의기준,기간으로 구성" style="width:100%">
						<caption>학습진행상황 목록</caption>
						<colgroup>
							<col width="80px" />
							<col width="" />
							<col width="100px" />
							<col width="100px" />
							<col width="80px" />
							<col width="160px" />
						</colgroup>
						<thead>
							<tr>
								<th scope="col">평가항목</th>
								<th scope="col">제목</th>
								<th scope="col">진행상황</th>
								<th scope="col">나의기준</th>
								<th scope="col">시험시간</th>
								<th scope="col">시험기간</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${exam_list}" var="result">
							<tr>
								<td>시험</td>
								<td class="left">
									<c:choose>
										<c:when test="${result.sulcheck eq 'N'}">
											<c:out value="${result.examtype eq 'M' ? '출석평가' : '온라인평가'}" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${result.indata eq 'Y'}">
													<!-- 제출 -->
													<!-- 미제출 -->
													<c:choose>
														<c:when test="${result.submitYn eq 'Y'}">
															<!-- 재응시 -->
															<!-- 응시 -->
															<c:choose>
																<c:when test="${result.userretry > 0}">
																	<c:out value="${result.examtype eq 'M' ? '출석평가' : '온라인평가'}" />
																</c:when>
																<c:otherwise>
																	<c:out value="${result.examtype eq 'M' ? '출석평가' : '온라인평가'}" />
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<c:out value="${result.examtype eq 'M' ? '출석평가' : '온라인평가'}" />
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<c:out value="${result.examtype eq 'M' ? '출석평가' : '온라인평가'}" />
												</c:otherwise>
											</c:choose>
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
														<c:when test="${result.userretry > 0}">
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
								<td>${fn2:getFormatDate(result.startdt, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.enddt, 'yyyy.MM.dd')}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${sul_list}" var="result">
							<tr>
								<td>설문</td>
								<td class="left">
									<c:if test="${result.eachcnt > 0}">
										${result.sulpapernm}
									</c:if>
									<c:if test="${result.eachcnt == 0}">
										<c:if test="${result.sultype == 1}">
											<c:if test="${result.progress <= result.tstep }">
												<c:if test="${result.indata ne 'Y'}">
													${result.sulpapernm}
												</c:if>
												<c:if test="${result.indata eq 'Y'}">
													${result.sulpapernm}
												</c:if>
											</c:if>
											<c:if test="${result.progress > result.tstep }">
												${result.sulpapernm}
											</c:if>
										</c:if>
										<c:if test="${result.sultype != 1}">
											<c:if test="${result.indata eq 'Y'}">
												${result.sulpapernm}
											</c:if>
											<c:if test="${result.indata ne 'Y'}">
												${result.sulpapernm}
											</c:if>
										</c:if>
									</c:if>
								</td>
								<td>
									<c:if test="${result.indata eq 'Y'}">
										<c:if test="${result.answerYn eq 'Y'}"><font color="blue">응시함</font></c:if>
										<c:if test="${result.answerYn ne 'Y'}"><font color="orange">응시가능</font></c:if>										
									</c:if>
									<c:if test="${result.indata ne 'Y'}">-</c:if>
								</td>
								<td>
									<c:if test="${result.answerYn eq 'Y'}"><font color="blue">응시(제출)</font></c:if>
									<c:if test="${result.answerYn ne 'Y'}">미응시</c:if>
								</td>
								<td>-</td>
								<td>${fn2:getFormatDate(result.aftersulsdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.aftersuledate, 'yyyy.MM.dd')}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${report_list}" var="result">
							<tr>
								<td>과제</td>
								<td class="left">
									<a href="#none" onclick="goReportInsertPage('${result.class}', '${result.ordseq}');">
									${result.title}
									</a>
								</td>
								<td>
									<c:if test="${result.indata eq 'Y'}"><font color="orange">응시가능</font></c:if>
									<c:if test="${result.indata ne 'Y'}">-</c:if>
								</td>
								<td>
									<c:if test="${result.answerYn eq 'Y'}"><font color="blue">응시</font></c:if>
									<c:if test="${result.answerYn ne 'Y'}">미응시</c:if>
								</td>
								<td>-</td>
								<td>${fn2:getFormatDate(result.startdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.expiredate, 'yyyy.MM.dd')}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			
            
                
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

	/* ********************************************************
	 * 공지사항
	 ******************************************************** */
	function noticeView(seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyNoticeView.do";
		frm.p_seq.value = seq;
		frm.target = "_self";
		frm.submit();
	}

	/* ********************************************************
	 * 자료실
	 ******************************************************** */
	function dataView(seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyDataView.do";
		frm.p_seq.value = seq;
		frm.target = "_self";
		frm.submit();
	}

	/* ********************************************************
	 * Q&A
	 ******************************************************** */
	function qnaView(seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyQnaView.do";
		frm.p_seq.value = seq;
		frm.target = "_self";
		frm.submit();
	}

	/* ********************************************************
	 * 시험
	 ******************************************************** */
	 /*
	function goExamAnswerPage(lesson, examtype, papernum, p_exam_subj){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyExamAnswerPage.do";
		
		var msg = "평가는 2회 응시가 가능합니다. (2회 응시가능)\n";
		msg += "평가 2회 응시 점수중 가장 높은 점수를 기준으로 점수가 반영됩니다.\n";
		msg += "한번열었다 닫아도 응시로 되오니(2회 응시가능)\n";
		msg += "시간여유 가지시고 응시하시기 바랍니다.";
		alert(msg);
		if( confirm("계속 진행하시겠습니까?") ){
			frm.p_lesson.value = lesson;
			frm.p_examtype.value = examtype;
			frm.p_papernum.value = papernum;
			frm.p_exam_subj.value = p_exam_subj;
			
			frm.target = "_self";
			frm.submit();
		}
	}
	*/

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
		
		alert(msg);
		
		if( confirm("계속 진행하시겠습니까?") ) {
			frm.p_lesson.value = lesson;
			frm.p_examtype.value = examtype;
			frm.p_papernum.value = papernum;
			frm.p_exam_subj.value = p_exam_subj;
			
			frm.target = "_self";
			frm.submit();
		}
	}
	
	/* ********************************************************
	 * 과제
	 ******************************************************** */
	function goReportInsertPage(v_class, ordseq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyReportInsertPage.do";
		
		frm.p_class.value = v_class;
		frm.p_ordseq.value = ordseq;
		
		frm.target = "_self";
		frm.submit();
	}

	/* ********************************************************
	 * 설문
	 ******************************************************** */
	function goSurveyInsertPage(v_subj, v_year, v_subjseq, v_sulpapernum){
		/*
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudySurveyInsertPage.do?pageType=pop";
		frm.p_subj.value = v_subj;
		frm.p_year.value = v_year;
		frm.p_subjseq.value = v_subjseq;
		frm.p_sulpapernum.value = v_sulpapernum;
		frm.p_subjnm.value = v_subjnm;
		frm.p_userid.value = v_userid;
		frm.target = "_self";
		frm.submit();
		*/
		changePage('04');
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