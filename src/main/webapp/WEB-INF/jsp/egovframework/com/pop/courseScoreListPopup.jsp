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
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
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

</form>


<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2" >
	
			
		<div class="popCon">
			<!-- header -->
<!--나의 학습창-->
<c:if test="${p_studymode eq 'only'}">
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
</c:if>	

<!--성적보기만 클릭시-->
<c:if test="${p_studymode ne 'only'}">
			<div class="myheader">
				<p class="mytit">성적정보</p>
			</div>
</c:if>	

			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				
				
				
				<div style="height:505px; overflow: auto;" tabindex="0">
				
				
				 <h4 class="tit">성적보기</h4>
				<!-- list -->
				<div class="tbList1">
					<table summary="구분, 수료기준, 취득점수, 반영비율, 총점, 조정점수로 구성" style="width:100%">
						<caption>
						성적정보
						</caption>
						<colgroup>
							<col width="25%"/>
							<col width="15%"/>
							<col width="15%"/>
							<col width="15%"/>
							<col width="15%"/>
							<col width="15%"/>
						</colgroup>
						<thead>
							<tr>
								<th scope="col">구분</th>
<!--								<th scope="col">수료기준</th>-->
								<th scope="col">평가배점</th>
								<th scope="col">취득점수</th>
								<th scope="col">총점</th>
								<th scope="col">조정점수</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>출석평가</td>
<!--								<td>${scoreData.gradexam}</td>-->
								<td>${scoreData.wmtest}</td>
								<td>${scoreData.mmtest}</td>
								<td rowspan="4" class="txtred">
								${scoreData.mscore}
								</td>
								<td rowspan="4">
									<c:if test="${scoreData.edutimes >= 60}">${scoreData.meditscore}</c:if>
									<c:if test="${scoreData.edutimes < 60}">-</c:if>
								</td>
							</tr>
                            <tr>
								<td>온라인평가</td>
<!--								<td>${scoreData.gradftest}</td>-->
								<td>${scoreData.wftest}</td>
								<td>${scoreData.mftest}</td>
							</tr>
                            <tr>
								<td>온라인과제</td>
<!--								<td>${scoreData.gradreport}</td>-->
								<td>${scoreData.wreport}</td>
								<td>
									<c:if test="${scoreData.mreportCount>0 and scoreData.mreport ==0 }">
										채점중
									</c:if>
									<c:if test="${scoreData.mreportCount>0 and scoreData.mreport > 0 }">
										${scoreData.mreport}
									</c:if>
									<c:if test="${scoreData.mreportCount == 0 }">
										${scoreData.mreport}
									</c:if>
								</td>
							</tr>
                            <tr>
								<td>진도율</td>
<!--								<td>${scoreData.wetc1}</td>-->
								<%-- <td>${scoreData.wetc2}</td> --%>
								<td>90% 이상</td>
								<%-- <td>
								<c:if test="${scoreData.tstep >= 80 }">
								${scoreData.metc2}
								</c:if>
								<c:if test="${scoreData.tstep < 80 }">
								0
								</c:if>
								</td> --%>
								<td>-</td>
							</tr>							
						</tbody>
					</table>
				</div>                
			  	<!-- // list -->
			  	
                
                <h4 class="tit">평가별 진행상황</h4>
                
                
                <!-- list -->
				<div class="tbList1">
					<table summary="출석평가, 온라인과제, 온라인평가, 설문으로 구성" width="100%">
						<caption>
						학습진행상황
						</caption>
						<colgroup>
							<col width="20%"/>
							<col width="30%"/>
							<col width="20%"/>
							<col width="30%"/>							
						</colgroup>
						<thead>
							<tr>
								<th scope="row">출석평가</th>
								<td class="left">
									<c:set var="checkCount" value="0" />
									<c:forEach items="${examdata}" var="result">
										<c:if test="${result.examtype eq 'C'}">
											<c:set var="checkCount" value="1" />
											<c:if test="${result.resultCount == 0}">미응시</c:if>
											<c:if test="${result.resultCount > 0}">
												<span class="txtblue">${result.examCount}</span> 개중 <span class="txtred">${result.resultCount}</span> 개 응시
											</c:if>
										</c:if>
									</c:forEach>
									<c:if test="${checkCount == 0 }">평가없음</c:if>
									
								</td>
								<th scope="row">온라인과제</th>
								<td class="left">
									<c:if test="${empty projdata}">평가없음</c:if>
									<c:if test="${not empty projdata}">
										<c:if test="${projdata.nosubmit > 0}">
											<span class="txtblue">${projdata.projCount}</span> 개중 <span class="txtred">${projdata.nosubmit}</span> 개 미응시
										</c:if>
										<c:if test="${projdata.resultCount > 0}">
											<span class="txtblue">${projdata.projCount}</span> 개중 <span class="txtred">${projdata.resultCount}</span> 개 응시
										</c:if>
									</c:if>
								</td>
							</tr>
                            <tr>
								<th scope="row">온라인평가</th>
								<td class="left">
									<c:set var="checkCount" value="0" />
									<c:forEach items="${examdata}" var="result">
										<c:if test="${result.examtype eq 'E'}">
											<c:set var="checkCount" value="1" />
											<c:if test="${result.resultCount == 0}">미응시</c:if>
											<c:if test="${result.resultCount > 0}">
												<span class="txtblue">${result.examCount}</span> 개중 <span class="txtred">${result.resultCount}</span> 개 응시
											</c:if>
										</c:if>
									</c:forEach>
									<c:if test="${checkCount == 0 }">평가없음</c:if>
								</td>
								<th scope="row">설문</th>
								<td class="left">
									<c:if test="${empty suldata}">설문없음</c:if>
									<c:if test="${not empty suldata}">
										<c:if test="${suldata.resultCount == 0}">미응시</c:if>
										<c:if test="${suldata.resultCount > 0}">
											<span class="txtblue">${suldata.sulCount}</span> 개중 <span class="txtred">${suldata.resultCount}</span> 개 응시
										</c:if>
									</c:if>
								</td>
							</tr>
						</thead>
						<tbody>							
						</tbody>
					</table>
				</div>

<!--			<h4 class="tit">출석정보</h4>-->
<!--			-->
<!--			-->
<!--			<div class="tbList1">-->
<!--				<c:forEach var="result" items="${checklist}" varStatus="status">-->
<!--				-->
<!--									<div class="sch_date_div" style="width:72px;">${fn2:getFormatDate(result.dateSeq, 'MM/dd')}-->
<!--										<P>${result.ist}</P>-->
<!--									</div>-->
<!--						-->
<!--				</c:forEach>-->
<!--			</div>-->
			
			<!-- 20170310 주석 -->
			<%--
			<h4 class="tit">참여도(진도율)점수 산출 기준</h4>
			
			
			<div class="tbList1">
				<table summary="출석평가, 온라인과제, 온라인평가, 설문으로 구성" width="100%">
						<caption>
						학습진행상황
						</caption>
						<colgroup>
							<col width="20%"/>
							<col width="40%"/>
							<col width="40%"/>
						</colgroup>
						<thead>
							<th scope="row">참여율</th>
							<th scope="row">0 ~ 80% 미만</th>
							<th scope="row">80% ~ 100%</th>
						</thead>
						<tbody>	
							<td>참여도점수</td>						
							<td>미이수(점수 없음)</td>						
							<td>나의 진도율 x (참여도점수 만점 x 0.01)</td>						
						</tbody>
				</table>
			</div> 
			--%>
			
            
			<c:set var="studyUse" value="N"></c:set>
			
			
			 <h4 class="tit">학습결과</h4>
			 
			 
			<div>
				<c:if test="${p_studyType eq 'NEW'}">
					<c:if test="${p_contenttype eq 'S'}">
						<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/newScormItemList.jsp" %>
					</c:if>
					<c:if test="${p_contenttype eq 'X'}">
						<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/newXiniceItemList.jsp" %>
					</c:if>
					<c:if test="${p_contenttype ne 'S' and p_contenttype ne 'X'}">
						<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/newNonScormItemList.jsp" %>
					</c:if>
				</c:if>
				<c:if test="${p_studyType eq 'OLD'}">
					<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/oldItemList.jsp" %>
				</c:if>
           	</div>
			<!-- //contents -->
			
			<br/>
		</div>
	</div>
</div>
</div>
</div>
<script type="text/javascript">
//<![CDATA[
	/* ********************************************************
	 * 과제
	 ******************************************************** */
	function goSurveyInsertPage(v_subj, v_year, v_subjseq, v_sulpapernum){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudySurveyInsertPage.do";
		frm.p_subj.value = v_subj;
		frm.p_year.value = v_year;
		frm.p_subjseq.value = v_subjseq;
		frm.p_sulpapernum.value = v_sulpapernum;
		frm.target = "_self";
		frm.submit();
	}

	function doStudyPopup(a,b,c) {}
	
	document.title="성적정보 보기("+"<c:out value='${EduScore.subjnm}' /> "+")";
//]]>
</script>
</body>
</html>

