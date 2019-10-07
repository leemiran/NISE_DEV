<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  					name="pageIndex"					value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 					name="p_process">
	<input type="hidden" id="p_create" 					name="p_create"						value="${p_create}">
	<input type="hidden" id="totalCnt" 					name="totalCnt"						value="${totalCnt}">
	<input type="hidden" id="search_att_name_value" name="search_att_name_value">

	
			
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="C"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->

	<div class="listTop">
		 <div class="btnR MR05">
		 			<span id="dataCreate" style="color:#ff0000; margin-right: 800px;"></span>
		 			<a href="#none" onclick="yearSave()" class="btn01"><span>저장</span></a>
		 			<a href="#none" onclick="dataCreate()" class="btn01"><span>데이터생성</span></a>
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
               		<a href="#none" onclick="yearDelete()" class="btn01"><span>삭제</span></a>
               		<!-- 
               		<a href="#" onclick="" class="btn01"><span>인쇄</span></a>
               		 -->
                </div>

  	</div>
  	
<!--계획 변수-->
<c:set var="sPlan" 			value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cPlan" 			value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fPlan" 			value="0"></c:set>	<!-- 무료과정 -->
<!--승인 변수-->
<c:set var="sChkfinal" 		value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cChkfinal" 		value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fChkfinal" 		value="0"></c:set>	<!-- 무료과정 -->
<!--실적 변수-->
<c:set var="sGraduated" 	value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cGraduated" 	value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fGraduated" 	value="0"></c:set>	<!-- 무료과정 -->
<!--과정내 이수율 변수-->
<c:set var="sSubChkfinal" 	value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cSubChkfinal" 	value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fSubChkfinal" 	value="0"></c:set>	<!-- 무료과정 -->
<!--이수율 변수-->
<c:set var="sSubComplet" value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cSubComplet" value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fSubComplet" value="0"></c:set>	<!-- 무료과정 -->
<!--종별 카운터 변수-->
<c:set var="sCount" 		value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cCount" 		value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fCount" 		value="0"></c:set>	<!-- 무료과정 -->

<c:set var="educationInsti" 			value="0"></c:set><!-- 교육청수입 -->
<c:set var="incomeAmount" 			value="0"></c:set><!-- 수입액 -->
<c:set var="contentDo" 				value="0"></c:set><!-- 만족도 -->
<c:set var="professorContentDo" 	value="0"></c:set><!-- 교수학습지도 향상 -->
<c:set var="propoCnt" 					value="0"></c:set><!-- 신청인원 -->
<c:set var="groupTotal" 				value="0"></c:set><!-- 단체신청인원(특별과정) -->
<c:set var="presentCnt" 				value="0"></c:set><!-- 제출인원 -->
<c:set var="normalProCnt" 			value="0"></c:set><!-- 일반수강인원 -->
<c:set var="groupProTottal" 			value="0"></c:set><!-- 단체신청인원 -->

		
		<!-- list table-->
		<div class="tbList" style="overflow:auto;height:700px;">
		  <table summary="" cellspacing="0" width="2000">
<colgroup>
	<col width="75"/>
	<col width="55"/>
	<col width="40"/>
	<col width="226"/>
	<col width="50"/>
	<col width="50"/>
	<col width="50"/> 
	<col width="60"/>
	<col width="70"/> 
	<col width="90"/>
	<col width="90"/>
	<col width="50"/>
	<col width="65"/>
	<col width="110"/>
	<col width="110"/>
	<col width="40"/>
	<col width="190"/>
	<col width="60"/>
	<col width="120"/>
	<col width="60"/>
	<col width="60"/>
	<col width="60"/>
	<col width="212"/>
	<col width="60"/>
</colgroup>

<thead>     
<tr>
<th rowspan="2" >구분</th>
<th rowspan="2" >종별</th>
<th rowspan="2" >기별</th>
<th rowspan="2" >과 정 명</th>
<th colspan="3" >연수인원</th>
<th rowspan="2" >과정내</br>이수율</th>	
<th rowspan="2" >이수율</th>
<th rowspan="2" > 교육청수입 </th>	
<th rowspan="2" > 수입액 </th>
<th rowspan="2" >만족도</th>
<th rowspan="2" >교수학습지도 향상도</th>
<th rowspan="2" >신청기간</th>
<th rowspan="2" >교육시기</th>	
<th rowspan="2" >시간</th>	
<th rowspan="2" >연수대상</th>	
<th colspan="4" >특별과정 신청현황</th>
<th colspan="3" >수강현황</th>
</tr>

<tr>
<th >계획(명)</th>
<th >승인(명)</th>
<th >실적(명)</th>
<th >신청</br>인원</th>
<th >단체신청내역</br>(특별과정)</th>
<th >단체신청인원</th>
<th >제출</br>인원</th>
<th >일반수강인원</th>
<th >단체신청인원</th>
<th >단체신청인원수</th>
</tr>
</thead>

<tbody>
<c:forEach var="result" items="${resultList}" varStatus="status">
<c:set var="educationInsti" 			value="${educationInsti		+result.educationInsti}"></c:set>
<c:set var="incomeAmount" 			value="${incomeAmount		+result.incomeAmount}"></c:set>
<c:set var="contentDo" 				value="${contentDo				+result.contentDo}"></c:set>
<c:set var="professorContentDo" 	value="${professorContentDo	+result.professorContentDo}"></c:set>
<c:set var="propoCnt" 					value="${propoCnt				+result.propoCnt}"></c:set>
<c:set var="groupTotal" 				value="${groupTotal				+result.groupTotal}"></c:set>
<c:set var="presentCnt" 				value="${presentCnt				+result.presentCnt}"></c:set>
<c:set var="normalProCnt" 			value="${normalProCnt			+result.normalProCnt}"></c:set>
<c:set var="groupProTottal" 			value="${groupProTottal		+result.groupProTotal}"></c:set>
<c:choose>
	<c:when test="${result.ischarge eq 'S'}">
		<c:set var="sPlan" 				value="${sPlan+result.planCnt}"></c:set>					<!-- 계획 -->
		<c:set var="sChkfinal" 			value="${sChkfinal+result.chkfinalCnt}"></c:set>			<!-- 승인 -->
		<c:set var="sGraduated" 		value="${sGraduated+result.graduatedCnt}"></c:set>	<!-- 실적 -->
		<c:set var="sSubChkfinal" 		value="${sSubChkfinal+result.subjChkfinal}"></c:set>	<!-- 과정냉 이수율 -->
		<c:set var="sSubComplet" 	value="${sSubComplet+result.subjPlan}"></c:set>	<!-- 이수율 -->
		<c:set var="sCount" 			value="${sCount+1}"></c:set>									<!-- 종별 카운터 -->
	</c:when>
	<c:when test="${result.ischarge eq 'C'}">
		<c:set var="cPlan" 				value="${cPlan+result.planCnt}"></c:set>					<!-- 계획 -->
		<c:set var="cChkfinal" 			value="${cChkfinal+result.chkfinalCnt}"></c:set>			<!-- 승인 -->
		<c:set var="cGraduated" 		value="${cGraduated+result.graduatedCnt}"></c:set>	<!-- 실적 -->
		<c:set var="cSubChkfinal" 		value="${cSubChkfinal+result.subjChkfinal}"></c:set>	<!-- 과정냉 이수율 -->
		<c:set var="cSubComplet" 	value="${cSubComplet+result.subjPlan}"></c:set>	<!-- 이수율 -->
		<c:set var="cCount" 			value="${cCount+1}"></c:set>									<!-- 종별 카운터 -->
	</c:when>
	<c:when test="${result.ischarge eq 'F'}">
		<c:set var="fPlan" 				value="${fPlan+result.planCnt}"></c:set>					<!-- 계획 -->
		<c:set var="fChkfinal" 			value="${fChkfinal+result.chkfinalCnt}"></c:set>			<!-- 승인 -->
		<c:set var="fGraduated" 		value="${fGraduated+result.graduatedCnt}"></c:set>	<!-- 실적 -->
		<c:set var="fSubChkfinal" 		value="${fSubChkfinal+result.subjChkfinal}"></c:set>		<!-- 과정냉 이수율 -->
		<c:set var="fSubComplet" 	value="${fSubComplet+result.subjPlan}"></c:set>	<!-- 이수율 -->
		<c:set var="fCount" 			value="${fCount+1}"></c:set>									<!-- 종별 카운터 -->
	</c:when>
	
	<c:otherwise>
		
	</c:otherwise>
</c:choose>

 	<tr>
		<td>${result.upperclassNm}</td>																											<!--구분 a-->
		<td>${result.ischargeNm}</td>																												<!--종별 b-->
		<td>${result.subjSeq}</td>																													<!--기별 c-->
		<td style="text-align: left">${result.subjNm}</td>																						<!--과정명 d-->
		<td><input type="text" name="planCnt_${result.grseq}_${result.subjseq}" 	id="planCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="planCnt_${result.upperclass}" value="${result.planCnt}" style="text-align: right" size="5" maxlength="5"></td><!--계획(e) -->
		<td><input type="text" name="chkfinalCnt_${result.grseq}_${result.subjseq}" 	id="chkfinalCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="chkfinalCnt_${result.upperclass}" value="${result.chkfinalCnt}" style="text-align: right" size="5" maxlength="5"></td><!--승인 f -->
		<td><input type="text" name="graduatedCnt_${result.grseq}_${result.subjseq}" 	id="graduatedCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="graduatedCnt_${result.upperclass}" value="${result.graduatedCnt}" style="text-align: right" size="5"  ></td><!--실적 g-->
		<td><c:if test="${result.subjChkfinal ne '0'}">${result.subjChkfinal}%</c:if></td><!--과정내이수율 h-->
		<td><input type="text" name="subjComplet_${result.grseq}_${result.subjseq}" 		id="subjComplet_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 		class="subjComplet_${result.upperclass}" value="${result.subjPlan}" style="text-align: center" size="5" readonly="readonly">%</td>	<!--이수율 i-->
		<td><input type="text" name="educationInsti_${result.grseq}_${result.subjseq}" 	id="educationInsti_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="educationInsti_${result.upperclass}" value="${result.educationInsti}" style="text-align: right" size="10" maxlength="12"></td>			<!--교육청수입 j-->
		<td><input type="text" name="incomeAmount_${result.grseq}_${result.subjseq}" 	id="incomeAmount_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="incomeAmount_${result.upperclass}"  value="${result.incomeAmount}" style="text-align: right" size="10" maxlength="12"></td>			<!--수입액 k-->
		<td><c:if test="${result.contentDo ne '00.0'}">${result.contentDo}%</c:if></td>											<!--만족도 l-->
		<td><c:if test="${result.professorContentDo ne '00.0'}">${result.professorContentDo}%</c:if></td>					<!--교수학습지도향상도 m-->
		<td>${result.propDate}</td>							<!--등록기간(수강신청기간) n-->
		<td>${result.eduDate}</td>								<!--연수시기(교육기간) o-->
		<td>${result.edutimes}</td>																													<!--시간 p-->
		<td>${result.edumans}</td>																													<!--연수대상 q-->
		<td><input type="text" name="propoCnt_${result.grseq}_${result.subjseq}" 		id="propoCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 		class="propoCnt_${result.upperclass}" value="${result.propoCnt}" 	style="text-align: right" size="6" maxlength="6"></td>			<!--신청인원 r-->
		<td><input type="text" name="groupCnt_${result.grseq}_${result.subjseq}" 		id="groupCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 		class="groupCnt_${result.upperclass}"  value="${result.groupCnt}"				style="text-align: center" maxlength="200"></td>		<!--단체신청내역 s-->
		<td><input type="text" name="groupTotal_${result.grseq}_${result.subjseq}" 	id="groupTotal_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 		class="groupTotal_${result.upperclass}"  value="${result.groupTotal}"				style="text-align: right" size="6"  maxlength="6"></td>		<!--단체신청인원-->
		<td><input type="text" name="presentCnt_${result.grseq}_${result.subjseq}" 	id="presentCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="presentCnt_${result.upperclass}" value="${result.presentCnt}" 	style="text-align: right" size="6" maxlength="6"></td>			<!--제출인원 t-->
		<td><input type="text" name="normalProCnt_${result.grseq}_${result.subjseq}" 	id="normalProCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="normalProCnt_${result.upperclass}" value="${result.normalProCnt}" style="text-align: right" size="5" maxlength="5"></td> <!--일반수강인원 u-->
		<td><input type="text" name="groupProCnt_${result.grseq}_${result.subjseq}" 	id="groupProCnt_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="groupProCnt_${result.upperclass}" value="${result.groupProCnt}" style="text-align: right" size="50" maxlength="50"></td> <!--단체신청인원 v-->
		<td><input type="text" name="groupProTotal_${result.grseq}_${result.subjseq}" 	id="groupProTotal_${result.grseq}_${result.upperclass}_${result.ischarge}_${result.subjseq}" 	class="groupProTotal_${result.upperclass}" value="${result.groupProTotal}" style="text-align: right" size="5" maxlength="5"></td> <!--단체신청인원 수 w-->
	</tr>
	<c:if test="${result.upperclass ne result.upperclassLead}">
	<tr style="background-color: #eeeeee; ">
		<td></td><!--구분 -->
		<td></td><!--종별 -->
		<td></td><!--기별 -->
		<td style="text-align: center"><strong>소계(${result.upperclassCnt})</strong></td><!--과정명 -->
		<td><input type="text" id="planCntTot_${result.upperclass}" value="${result.planTotal}" style="text-align: right" size="5" 		readonly="readonly"></td><!--계획 -->
		<td><strong><fmt:formatNumber value="${result.chkfinalTotal}"  pattern="#,###"/></strong></td><!--승인 -->
		<td><strong><fmt:formatNumber value="${result.graduatedTotal}"  pattern="#,###"/></strong></td><!--실적 -->
		<td><strong><fmt:formatNumber value="${result.subjChkfinalAvg}"  pattern="#,###.#"/>%</strong></td><!--과정내이수율 -->
		<td><input type="text" id="subjCompletTot_${result.upperclass}" value="<fmt:formatNumber value="${result.subjPlanAvg}"  pattern="#,###.#"/>" style="text-align: center" size="5" 	readonly="readonly">%</td><!--이수율 -->
		<td><input type="text" id="educationInstiTot_${result.upperclass}" value="<fmt:formatNumber value="${result.educationInstiTotal}"  pattern="#,###.#"/>" style="text-align: right" size="10" 	readonly="readonly"></td><!--교육청수입 -->
		<td><input type="text" id="incomeAmountTot_${result.upperclass}" value="${result.incomeAmountTotal}" style="text-align: right" size="10" 	readonly="readonly"></td><!--수입액 -->
		<td><strong>${result.contentDoAvg}%</strong></td><!--만족도 -->
		<td><strong>${result.professorContentDoAvg}%</strong></td><!--교수학습지도향상도 -->
		<td></td><!--등록기간(수강신청기간) -->
		<td></td><!--연수시기(교육기간) -->
		<td></td><!--시간 -->
		<td></td><!--연수대상 -->
		<td><input type="text" id="propoCntTot_${result.upperclass}" value="${result.propoTotal}" 	style="text-align: right" size="6" 	readonly="readonly"></td><!--신청인원 -->
		<td></td><!--단체신청내역 -->
		<td><input type="text" id="groupTotalTot_${result.upperclass}" 		value="${result.groupTotalCnt}"		style="text-align: right" 	size="6" 		readonly="readonly"></td><!--단체신청인원 -->
		<td><input type="text" id="presentCntTot_${result.upperclass}" value="${result.presentTotal}" 	style="text-align: right" size="6" 	readonly="readonly"></td><!--제출인원 -->
		<td><!--일반수강인원 -->
				<strong><fmt:formatNumber value="${result.normalProTotal}"  pattern="#,###"/></strong>
		</td>
		<td colspan="2"><strong><fmt:formatNumber value="${result.groupProTotalCnt}"  pattern="#,###"/></strong></td><!--단체신청인원 -->
	</tr>
	</c:if>
</c:forEach>
<c:if test="${not empty resultList}">
<tr style="background-color: #ccccdd; ">
		<td></td><!--구분 -->
		<td></td><!--종별 -->
		<td></td><!--기별 -->
		<td style="text-align: center"><strong>시.도 특별과정 계</strong></td><!--과정명 -->
		<td><strong>
		<input type="text" id="planCntTot_s" value="<c:if test="${sPlan ne 0}"><fmt:formatNumber value="${sPlan}"  pattern="#,###"/></c:if><c:if test="${sPlan eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">
		</strong></td><!--계획 -->
		<td><strong><fmt:formatNumber value="${sChkfinal}"  pattern="#,###.#"/></strong></td><!--승인 -->
		<td><strong><fmt:formatNumber value="${sGraduated}"  pattern="#,###.#"/></strong></td><!--실적 -->
		<td>
			<strong>
							<c:if test="${sSubChkfinal ne 0 && sCount ne 0}"><fmt:formatNumber value="${sSubChkfinal/sCount}"  pattern="#,###.#"/></c:if>
							<c:if test="${sSubChkfinal eq 0 || sCount eq 0}">0</c:if>%
			</strong>
		</td><!--과정내이수율 -->
		<td>
			<input type="text" id="subjCompletTot_s" value="<c:if test="${sSubComplet ne 0 && sCount ne 0}"><fmt:formatNumber value="${sSubComplet/sCount}"  pattern="#,###.#"/>%</c:if><c:if test="${sSubComplet eq 0 || sCount eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">%
		</td><!--이수율 -->
		<td></td><!--교육청수입 -->
		<td></td><!--수입액 -->
		<td><strong></strong></td><!--만족도 -->
		<td><strong></strong></td><!--교수학습지도향상도 -->
		<td></td><!--등록기간(수강신청기간) -->
		<td></td><!--연수시기(교육기간) -->
		<td></td><!--시간 -->
		<td></td><!--연수대상 -->
		<td></td><!--신청인원 -->
		<td></td><!--단체신청내역 -->
		<td></td><!--단체신청인원 -->
		<td></td><!--제출인원 -->
		<td></td><!--일반수강인원 -->		
		<td colspan="2"></td><!--단체신청인원 -->
	</tr>
	<tr style="background-color: #ccccdd; ">
		<td></td><!--구분 -->
		<td></td><!--종별 -->
		<td></td><!--기별 -->
		<td style="text-align: center"><strong>일반과정 계(정규+무료)</strong></td><!--정규c+무료f-->
		<td><strong>
		<input type="text" id="planCntTot_cf" value="<c:if test="${(cPlan+fPlan) ne 0}"><fmt:formatNumber value="${cPlan+fPlan}"  pattern="#,###"/></c:if><c:if test="${(cPlan+fPlan) eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">
		</strong></td><!--계획 -->
		<td><strong><fmt:formatNumber value="${cChkfinal+fChkfinal}"  pattern="#,###"/></strong></td><!--승인 -->
		<td><strong><fmt:formatNumber value="${cGraduated+fGraduated}"  pattern="#,###"/></strong></td><!--실적 -->
		<td>
			<strong>
							<c:if test="${(cSubChkfinal+fSubChkfinal) ne 0 && (cCount+fCount) ne 0}"><fmt:formatNumber value="${(cSubChkfinal+fSubChkfinal)/(cCount+fCount)}"  pattern="#,###.#"/></c:if>
							<c:if test="${(cSubChkfinal+fSubChkfinal) eq 0 || (cCount+fCount) eq 0}">0</c:if>%
			</strong>
		</td><!--과정내이수율 -->
		<td>
			<input type="text" id="subjCompletTot_cf" value="<c:if test="${(cSubComplet+fSubComplet) ne 0 && (cCount+fCount) ne 0}"><fmt:formatNumber value="${(cSubComplet+fSubComplet)/(cCount+fCount)}"  pattern="#,###.#"/>%</c:if><c:if test="${(cSubComplet+fSubComplet) eq 0 || (cCount+fCount) eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">%
		</td><!--이수율 -->
		<td></td><!--교육청수입 -->
		<td></td><!--수입액 -->
		<td><strong></strong></td><!--만족도 -->
		<td><strong></strong></td><!--교수학습지도향상도 -->
		<td></td><!--등록기간(수강신청기간) -->
		<td></td><!--연수시기(교육기간) -->
		<td></td><!--시간 -->
		<td></td><!--연수대상 -->
		<td></td><!--신청인원 -->
		<td></td><!--단체신청내역 -->
		<td></td><!--단체신청인원 -->
		<td></td><!--제출인원 -->
		<td></td><!--일반수강인원 -->		
		<td colspan="2"></td><!--단체신청인원 -->
	</tr>
	<tr style="background-color: #ccddcc; ">
		<td></td><!--구분 -->
		<td></td><!--종별 -->
		<td></td><!--기별 -->
		<td style="text-align: center"><strong>유료과정 계(정규+특별)</strong></td><!--정규c+특별s -->
		<td><strong>
		<input type="text" id="planCntTot_cs" value="<c:if test="${(cPlan+sPlan) ne 0}"><fmt:formatNumber value="${cPlan+sPlan}"  pattern="#,###"/></c:if><c:if test="${(cPlan+sPlan) eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">
		</strong></td><!--계획 -->
		<td><strong><fmt:formatNumber value="${cChkfinal+sChkfinal}"  pattern="#,###"/></strong></td><!--승인 -->
		<td><strong><fmt:formatNumber value="${cGraduated+sGraduated}"  pattern="#,###"/></strong></td><!--실적 -->
		<td>
			<strong>
							<c:if test="${(cSubChkfinal+sSubChkfinal) ne 0 && (cCount+sCount) ne 0}"><fmt:formatNumber value="${(cSubChkfinal+sSubChkfinal)/(cCount+sCount)}"  pattern="#,###.#"/></c:if>
							<c:if test="${(cSubChkfinal+sSubChkfinal) eq 0 || (cCount+sCount) eq 0}">0</c:if>%
			</strong>
		</td><!--과정내이수율 -->
		<td>
			<input type="text" id="subjCompletTot_cs" value="<c:if test="${(cSubComplet+sSubComplet) ne 0 && (cCount+sCount) ne 0}"><fmt:formatNumber value="${(cSubComplet+sSubComplet)/(cCount+sCount)}"  pattern="#,###.#"/>%</c:if><c:if test="${(cSubComplet+sSubComplet) eq 0 || (cCount+sCount) eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">%
		</td><!--이수율 -->
		<td></td><!--교육청수입 -->
		<td></td><!--수입액 -->
		<td><strong></strong></td><!--만족도 -->
		<td><strong></strong></td><!--교수학습지도향상도 -->
		<td></td><!--등록기간(수강신청기간) -->
		<td></td><!--연수시기(교육기간) -->
		<td></td><!--시간 -->
		<td></td><!--연수대상 -->
		<td></td><!--신청인원 -->
		<td></td><!--단체신청내역 -->
		<td></td><!--단체신청인원 -->
		<td></td><!--제출인원 -->
		<td></td><!--일반수강인원 -->		
		<td colspan="2"></td><!--단체신청인원 -->
	</tr>
	<tr style="background-color: #ccddcc; ">
		<td></td><!--구분 -->
		<td></td><!--종별 -->
		<td></td><!--기별 -->
		<td style="text-align: center"><strong>무료과정 계</strong></td><!--과정명 -->
		<td><strong>
		<input type="text" id="planCntTot_f" value="<c:if test="${fPlan ne 0}"><fmt:formatNumber value="${fPlan}"  pattern="#,###"/></c:if><c:if test="${fPlan eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">
		</strong></td><!--계획 -->
		<td><strong><fmt:formatNumber value="${fChkfinal}"  pattern="#,###"/></strong></td><!--승인 -->
		<td><strong><fmt:formatNumber value="${fGraduated}"  pattern="#,###"/></strong></td><!--실적 -->
		<td>
			<strong>
							<c:if test="${fSubChkfinal ne 0 && fCount ne 0}"><fmt:formatNumber value="${fSubChkfinal/fCount}"  pattern="#,###.#"/></c:if>
							<c:if test="${fSubChkfinal eq 0 || fCount eq 0}">0</c:if>%
			</strong>
		</td><!--과정내이수율 -->
		<td>
			<input type="text" id="subjCompletTot_f" value="<c:if test="${fSubComplet ne 0 && fCount ne 0}"><fmt:formatNumber value="${fSubComplet/fCount}"  pattern="#,###.#"/>%</c:if><c:if test="${fSubComplet eq 0 || fCount eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">%
		</td><!--이수율 -->
		<td></td><!--교육청수입 -->
		<td></td><!--수입액 -->
		<td><strong></strong></td><!--만족도 -->
		<td><strong></strong></td><!--교수학습지도향상도 -->
		<td></td><!--등록기간(수강신청기간) -->
		<td></td><!--연수시기(교육기간) -->
		<td></td><!--시간 -->
		<td></td><!--연수대상 -->
		<td></td><!--신청인원 -->
		<td></td><!--단체신청내역 -->
		<td></td><!--단체신청인원 -->
		<td></td><!--제출인원 -->
		<td></td><!--일반수강인원 -->		
		<td colspan="2"></td><!--단체신청인원 -->
	</tr>
	<tr style="background-color: #aaaaaa; ">
		<td></td><!--구분 -->
		<td></td><!--종별 -->
		<td></td><!--기별 -->
		<td style="text-align: center"><strong>전체 소계(${sCount+cCount+fCount})</strong></td><!-- -->
		<td><strong>
		<input type="text" id="planCntTot_scf" value="<c:if test="${(sPlan+cPlan+fPlan) ne 0}"><fmt:formatNumber value="${sPlan+cPlan+fPlan}"  pattern="#,###"/></c:if><c:if test="${(sPlan+cPlan+fPlan) eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">
		</strong></td><!--계획 총계-->
		<td><strong><fmt:formatNumber value="${sChkfinal+cChkfinal+fChkfinal}"  pattern="#,###"/></strong></td><!--승인 총계-->
		<td><strong><fmt:formatNumber value="${sGraduated+cGraduated+fGraduated}"  pattern="#,###"/></strong></td><!--실적 총계-->
		<td>
			<strong>
							<c:if test="${(sSubChkfinal+cSubChkfinal+fSubChkfinal) ne 0 && (sCount+cCount+fCount) ne 0}"><fmt:formatNumber value="${(sSubChkfinal+cSubChkfinal+fSubChkfinal)/(sCount+cCount+fCount)}"  pattern="#,###.#"/></c:if>
							<c:if test="${(sSubChkfinal+cSubChkfinal+fSubChkfinal) eq 0 || (sCount+cCount+fCount) eq 0}">0</c:if>%
			</strong>
		</td><!--과정내이수율 -->
		<td>
			<input type="text" id="subjCompletTot_scf" value="<c:if test="${(sSubComplet+cSubComplet+fSubComplet) ne 0 && (sCount+cCount+fCount) ne 0}"><fmt:formatNumber value="${(sSubComplet+cSubComplet+fSubComplet)/(sCount+cCount+fCount)}"  pattern="#,###.#"/>%</c:if><c:if test="${(sSubComplet+cSubComplet+fSubComplet) eq 0 || (sCount+cCount+fCount) eq 0}">0</c:if>" style="text-align: center" size="5" 	readonly="readonly">%
		</td><!--이수율 -->
		<td><strong>
		<input type="text" id="educationInstiTot_scf" value="<fmt:formatNumber value="${educationInsti}"  pattern="#,###"/>" style="text-align: center" size="10" readonly="readonly">
		</strong></td><!--교육청수입 -->
		<td><strong>
		<input type="text" id="incomeAmountTot_scf" value="<fmt:formatNumber value="${incomeAmount}"  pattern="#,###"/>" style="text-align: center"   size="10"	readonly="readonly">
		</strong></td><!--수입액 -->
		<td><strong><fmt:formatNumber value="${contentDo/(sCount+cCount+fCount)}"  pattern="#,###.#"/>%</strong></td><!--만족도 -->
		<td><strong><fmt:formatNumber value="${professorContentDo/(sCount+cCount+fCount)}"  pattern="#,###.#"/>%</strong></td><!--교수학습지도향상도 -->
		<td></td><!--등록기간(수강신청기간) -->
		<td></td><!--연수시기(교육기간) -->
		<td></td><!--시간 -->
		<td></td><!--연수대상 -->
		<td><strong><input type="text" id="propoCntTot_scf" value="<fmt:formatNumber value="${propoCnt}"  pattern="#,###"/>" style="text-align: center" size="6" 	readonly="readonly"></strong></td><!--신청인원 -->
		<td></td><!--단체신청내역 -->
		<td><strong><input type="text" id="groupTotalTot_scf" value="<fmt:formatNumber value="${groupTotal}"  pattern="#,###"/>" style="text-align: center" size="6"  	readonly="readonly"></strong></td><!--단체신청인원 -->		
		<td><strong><input type="text" id="presentCntTot_scf" value="<fmt:formatNumber value="${presentCnt}"  pattern="#,###"/>" style="text-align: center" size="6" 	readonly="readonly"></strong></td><!--제출인원 -->
		<td><strong><fmt:formatNumber value="${normalProCnt}"  pattern="#,###"/></strong></td><!--일반수강인원 -->		
		<td colspan="2"><strong><fmt:formatNumber value="${groupProTottal}"  pattern="#,###"/></strong></td><!--단체신청인원 -->
	</tr>
</c:if>	
<c:if test="${empty resultList}">
	<tr>
		<td colspan="22" align="center">조회된 내용이 없습니다.</td>
	</tr>
</c:if>
</tbody>
</table>
<br><br><Br>
		</div>
		<!-- list table-->
</form>

<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--
var timecnt=0;
$("#ses_search_gyear option:eq(0)").remove();
var year = $("#ses_search_gyear").val();
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
if("${totalCnt}" > 0 & thisForm.p_create.value == "C"){	
	$("#dataCreate").css("color","#00aa00");
	$("#dataCreate").text(year+"년도 운영 현황 데이터 생성 완료");
	setTimeout("resultTime()", 3000);
}

$(function(){
	$("input").change(function(){
		//key[1] : grseq , key[2] : upperclass , key[3] : ischarge
		var key = new String($(this).attr("id")).split("_");
		var sumTot = 0;
		$("."+key[0]+"_"+key[2]).each(function(){
			sumTot += Number($(this).val());
			
		});
		if(key[0] != "groupCnt"){
			var sum = new String(sumTot).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
			$("#"+key[0]+"Tot_"+key[2]).val(sum);
		}
		
		//이수율 계산
		if(key[0] == "planCnt"){
			var pam1= $("#graduatedCnt_"+key[1]+"_"+key[2]+"_"+key[3]+"_"+key[4]).val();
			if($(this).val() != 0 && pam1 != 0){
				var vag =((pam1/$(this).val())*100).toFixed(1); 
				$("#subjComplet_"+key[1]+"_"+key[2]+"_"+key[3]+"_"+key[4]).val(vag);
				sumTot =0;
				var cnt=0;
					$(".subjComplet_"+key[2]).each(function(){
						//if( Number($(this).val()) > 0){
							sumTot += Number($(this).val());
							cnt++;
						//}
					});
				$("#subjCompletTot_"+key[2]).val((sumTot/cnt).toFixed(1));
			}else if($(this).val() == 0){
				$("#subjComplet_"+key[1]+"_"+key[2]+"_"+key[3]+"_"+key[4]).val(0);
				sumTot =0;
				var cnt=0;
					$(".subjComplet_"+key[2]).each(function(){
						//if( Number($(this).val()) > 0){
							sumTot += Number($(this).val());
							cnt++;
						//}
					});
					if(sumTot == 0){
						$("#subjCompletTot_"+key[2]).val(0);
					}else{
						$("#subjCompletTot_"+key[2]).val((sumTot/cnt).toFixed(1));
					}
				
			}
		}else{
			var multiSum = 0;
			$("input[name^='"+key[0]+"_']").each(function(){
				multiSum += Number($(this).val());
			});
			var sum = new String(multiSum).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
			$("#"+key[0]+"Tot_scf").val(sum);
			
		}
		
		totalSum();//계획인원 계산
	}); 
	
});

function totalSum(){
	var cnts = 0;
	var cntc = 0;
	var cntf = 0;
	var planTot_s = 0;
	var planTot_c = 0;
	var planTot_f = 0;
	$("input[name^='planCnt_']").each(function(){
		var plan = new String($(this).attr("id")).split("_");
		if( plan[3] == "S"){
			planTot_s += Number($(this).val());
			cnts++;
		}else if( plan[3] == "C"){
			planTot_c += Number($(this).val());
			cntc++;
		}else if( plan[3] == "F"){
			planTot_f += Number($(this).val());
			cntf++;
		}
	});
	$("#planCntTot_s").val(planTot_s);									//시도특별과정
	$("#planCntTot_cf").val(planTot_c+planTot_f);					//일반과정 계(정규c+무료f)
	$("#planCntTot_cs").val(planTot_c+planTot_s);					//유료과정 계(정규+특별)
	$("#planCntTot_f").val(planTot_f);									//무료과정
	$("#planCntTot_scf").val(planTot_s+planTot_c+planTot_f);	//전체
	
	cnts = 0;
	cntc = 0;
	cntf = 0;
	var completTot_s = 0;
	var completTot_c = 0;
	var completTot_f = 0;
	$("input[name^='subjComplet_']").each(function(){
		var plan = new String($(this).attr("id")).split("_");
		if(Number($(this).val()) > 0 && plan[3] == "S"){
			completTot_s += Number($(this).val());
			cnts++;
		}else if(Number($(this).val()) > 0 && plan[3] == "C"){
			completTot_c += Number($(this).val());
			cntc++;
		}else if(Number($(this).val()) > 0 && plan[3] == "F"){
			completTot_f += Number($(this).val());
			cntf++;
		}
	});
	var complet_tot = completTot_s+completTot_c+completTot_f;
	var totcnt = cnts+cntc+cntf;
	var vag_s = (completTot_s/cnts).toFixed(1); 
	var vag_cf =  ((completTot_c+completTot_f)/(cntc+cntf)).toFixed(1); 
	var vag_cs =  ((completTot_c+completTot_s)/(cntc+cnts)).toFixed(1); 
	var vag_f = (completTot_f/cntf).toFixed(1); 
	var vag_all =  (complet_tot/totcnt).toFixed(1); 
	if(!isNaN(vag_s))$("#subjCompletTot_s").val(vag_s);		//시도특별과정
	if(!isNaN(vag_cf))$("#subjCompletTot_cf").val(vag_cf);	//일반과정 계(정규c+무료f)
	if(!isNaN(vag_cs))$("#subjCompletTot_cs").val(vag_cs);	//유료과정 계(정규+특별)
	if(!isNaN(vag_f))$("#subjCompletTot_f").val(vag_f);		//무료과정
	if(!isNaN(vag_all))$("#subjCompletTot_scf").val(vag_all);	//전체
}


function resultTime(){
	$("#dataCreate").css("color","#ff0000");
	$("#dataCreate").text("");
}
/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	if($("#totalCnt").val() == 0){
		alert("조회된 내역이 없습니다.");
		return;
	}
	thisForm.action = "/adm/com/main/admYearEduStatusExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {	
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.p_create.value="L";
	thisForm.action = "/adm/com/main/admYearEduStatusList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/com/main/admYearEduStatus.do";
	thisForm.target = "_self";
	thisForm.submit();	
	
}

function dataCreate(pageNo){
	if(!confirm(year+"년도 운영 현황의 데이터를 생성하면 \n기존에 등록된 내역은 삭제 됩니다. \n생성 하시겠습니까?(생성시 1분정도의 시간이 소요될 수 있습니다)"))return;
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	var timecnt = 0;
	setInterval(function(){ 
		if($("#dataCreate").text() == ""){
			$("#dataCreate").text("당해 연도 운영 현황 데이터 생성 중");
		}else{
			$("#dataCreate").text("");
		}	
		timecnt = timecnt+1;
	}, 1000);
	thisForm.action = "/adm/com/main/admYearEduStatus.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.p_create.value="C";
	thisForm.target = "_self";
	thisForm.submit();
}

function yearSave(pageNo){
	if($("#totalCnt").val() == 0){
		alert("조회된 내역이 없습니다.");
		return;
	}
	if(!confirm(year+"년도 운영 현황의 수정된 내역을 저장 하시겠습니까?"))return;
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/com/main/admYearEduStatusUpdate.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.p_create.value="U";
	thisForm.target = "_self";
	thisForm.submit();
}

function yearDelete(){
	if($("#totalCnt").val() == 0){
		alert("조회된 내역이 없습니다.");
		return;
	}
	if(!confirm(year+"년도 운영 현황 내역을 삭제 하시겠습니까?"))return;
	thisForm.action = "/adm/com/main/admYearEduStatusDelete.do";
	thisForm.pageIndex.value = "1";
	thisForm.p_create.value="D";
	thisForm.target = "_self";
	thisForm.submit();
}
//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
