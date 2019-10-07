<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 			name="p_process">
	<input type="hidden" id="search_att_name_value" name="search_att_name_value">

	
			
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="C"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->

	<div class="listTop">
		 <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
               		<a href="#" onclick="" class="btn01"><span>인쇄</span></a>
                </div>

  	</div>

<!--		<div class="listTop">-->
<!--                <div class="btnR MR05">-->
<!--               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>-->
<!--                </div>-->
<!--  		</div>-->
  		
  		
		
		<!-- list table-->
		<div class="tbList" style="overflow:auto;height:500px;">
		  <table summary="" cellspacing="0" width="3000">
<colgroup>
	<col width="93"/>
	<col width="81"/>
	<col width="46"/>
	<col width="236"/>
	<col width="81"/>
	<col width="81"/>
	<col width="81"/>
	<col width="113"/>
	<col width="81"/>
	<col width="133"/>
	<col width="133"/>
	<col width="69"/>
	<col width="81"/>
	<col width="131"/>
	<col width="154"/>
	<col width="71"/>
	<col width="206"/>
	<col width="90"/>
	<col width="179"/>
	<col width="233"/>
	<col width="100"/>
	<col width="222"/>
	<col width="146"/>
	<col width="81"/>
</colgroup>


<thead>     
<tr>
<th rowspan="2" >구분</th>
<th rowspan="2" >종별</th>
<th rowspan="2" >기별</th>
<th rowspan="2" >과 정 명</th>
<th colspan="3" >연수인원</th>
<th rowspan="2" >과정내 이수율</th>
<th rowspan="2" >이수율</th>
<th rowspan="2" > 교육청수입 </th>
<th rowspan="2" > 수입액 </th>
<th rowspan="2" >만족도</th>
<th rowspan="2" >교수학습지도 향상도</th>
<th rowspan="2" >등록기간</th>
<th rowspan="2" >연수시기</th>
<th rowspan="2" >시간</th>
<th rowspan="2" >연수대상</th>
<th colspan="3" >특별과정 신청현황</th>
<th colspan="2" >수강현황</th>
</tr>

<tr>
<th >계획(명)</th>
<th >승인(명)</th>
<th >실적(명)</th>
<th  >신청인원</th>
<th >단체신청내역(특별과정)</th>
<th >제출인원</th>
<th >일반수강인원</th>
<th >단체신청인원</th>
</tr>
</thead>

<tbody>
<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >1</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >400</td>
<td >257</td>
<td >244</td>
<td >94.9%</td>
<td >61.0%</td>
<td > 12,200,000 </td>
<td > 12,200,000 </td>
<td >85.6</td>
<td >87.2 </td>
<td >3.3-3.14</td>
<td >3.24(<span>월</span><span>)~5.2(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >해당 시<span>·</span><span>도 통합교사</span></td>
<td >1,000 </td>
<td >경남(1,000)</td>
<td >경남(264)</td>
<td > </td>
<td >경남(257)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >2</td>
<td >일반학교에서의 장애학생 지원</td>
<td >200</td>
<td >75</td>
<td >73</td>
<td >97.3%</td>
<td >36.5%</td>
<td > 2,050,000 </td>
<td > 4,090,000 </td>
<td >79.8</td>
<td >83.6 </td>
<td >3.17-3.28</td>
<td >4.7(<span>월</span><span>)~5.3(</span><span>금</span><span>)</span></td>
<td  >61(4주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(44)</td>
<td >34 </td>
<td >부산(41)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >3</td>
<td >특수교육대상학생의 사회과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >86</td>
<td >81</td>
<td >94.2%</td>
<td >81.0%</td>
<td > 1,530,000 </td>
<td > 3,870,000 </td>
<td >77.4</td>
<td >80.4 </td>
<td >3.17-3.28</td>
<td >3.31(<span>월</span><span>)~4.18(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(37)</td>
<td >52 </td>
<td >부산(34)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >4</td>
<td >특수교육대상학생의 과학과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >64</td>
<td >62</td>
<td >96.9%</td>
<td >62.0%</td>
<td > 900,000 </td>
<td > 2,880,000 </td>
<td >79</td>
<td >75.0 </td>
<td >3.17-3.28</td>
<td >3.31(<span>월</span><span>)~4.18(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(21)</td>
<td >44 </td>
<td >부산(20)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >5</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >1,600</td>
<td >1,472</td>
<td >1,355</td>
<td >92.1%</td>
<td >84.7%</td>
<td > 67,750,000 </td>
<td > 67,750,000 </td>
<td >77.8</td>
<td >80.3 </td>
<td >3.31-4.11</td>
<td >4.21(<span>월</span><span>)~5.30(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >해당 시<span>·</span><span>도 통합교사</span></td>
<td >1,600 </td>
<td >서울(1,000), 강원(600)</td>
<td >서울(901), 강원(594)</td>
<td > </td>
<td >서울(885), 강원(587)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >6</td>
<td >장애학생 진로<span>·</span><span>직업교육</span></td>
<td >200</td>
<td >240</td>
<td >228</td>
<td >95.0%</td>
<td >114.0%</td>
<td > 3,510,000 </td>
<td > 10,575,000 </td>
<td >78</td>
<td >81.6 </td>
<td >3.31-4.11</td>
<td >4.14(<span>월</span><span>)~5.2(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산교육청(85명)</td>
<td >157 </td>
<td >부산교육청(83명)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >7</td>
<td >치료교육 표시과목 변경 직무연수</td>
<td >5</td>
<td >4</td>
<td >4</td>
<td >100.0%</td>
<td >80.0%</td>
<td > 240,000 </td>
<td > 240,000 </td>
<td >80</td>
<td >64.0 </td>
<td >4.28-5.9</td>
<td >5.19(<span>월</span><span>)~6.14(</span><span>금</span><span>)</span></td>
<td  >61(4주)</td>
<td >치료교육 표시과목 미변경자</td>
<td  > </td>
<td >교육부(5)</td>
<td >울산(1),강원(3)</td>
<td > </td>
<td >울산(1),강원(3)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >8</td>
<td >장애학생 이해와 통합학급 운영</td>
<td >200</td>
<td >126</td>
<td >114</td>
<td >90.5%</td>
<td >57.0%</td>
<td > 1,100,000 </td>
<td > 7,100,000 </td>
<td >77</td>
<td >82.3 </td>
<td >4.28-5.9</td>
<td >5.19(<span>월</span><span>)~6.14(</span><span>금</span><span>)</span></td>
<td  >61(4주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(27)</td>
<td >100 </td>
<td >부산(26)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >9</td>
<td >특수교육대상학생의 국어과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >178</td>
<td >174</td>
<td >97.8%</td>
<td >174.0%</td>
<td > 4,050,000 </td>
<td > 8,010,000 </td>
<td >77</td>
<td >82.4 </td>
<td >4.28-5.9</td>
<td >5.12(<span>월</span><span>)~5.30(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(90)</td>
<td >88 </td>
<td >부산(90)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >10</td>
<td >특수교육대상학생의 수학과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >169</td>
<td >161</td>
<td >95.3%</td>
<td >161.0%</td>
<td > 2,070,000 </td>
<td > 7,515,000 </td>
<td >81.4</td>
<td >84.3 </td>
<td >4.28-5.9</td>
<td >5.12(<span>월</span><span>)~5.30(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(49)</td>
<td >121 </td>
<td >부산(48)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >11</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >1,340</td>
<td >1,315</td>
<td >1,279</td>
<td >97.3%</td>
<td >95.4%</td>
<td > 63,950,000 </td>
<td > 63,950,000 </td>
<td >80.8</td>
<td >81.4 </td>
<td >5.7-5.20</td>
<td >5.7(<span>월</span><span>)~6.13(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >해당 시<span>·</span><span>도 통합교사</span></td>
<td >1,340 </td>
<td >경기(1,340)</td>
<td >경기(1,340)</td>
<td > </td>
<td >경기(1,315)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >12</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >1,000</td>
<td >1,842</td>
<td >1,775</td>
<td >96.4%</td>
<td >177.5%</td>
<td > 88,750,000 </td>
<td > 88,750,000 </td>
<td >81.6</td>
<td >82.6 </td>
<td >5.12-5.23</td>
<td >6.2(<span>월</span><span>)~7.11(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >해당 시<span>·</span><span>도 통합교사</span></td>
<td >1,718 </td>
<td >광주(700), 부산(300), 충북(718)</td>
<td >광주(464), 부산(737), 충북(741)</td>
<td > </td>
<td >광주(437), 부산(706), 충북(699)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >13</td>
<td >특수교육교원 상위자격 취득을 위한 사전연수</td>
<td >100</td>
<td >68</td>
<td >67</td>
<td >98.5%</td>
<td >67.0%</td>
<td > 4,080,000 </td>
<td > 4,080,000 </td>
<td >75.2</td>
<td >76.2 </td>
<td >5.26-6.6</td>
<td >6.16(<span>월</span><span>)~7.12(</span><span>금</span><span>)</span></td>
<td  >61(4주)</td>
<td >희소과목 자격증 소지자</td>
<td  > </td>
<td >미정</td>
<td >서울(8), 광주(1), 부산(11), 대전(2), 인천(1), 제주(6), 강원(6), 충북(2), 충남(3), 경북(3), 경남(1), 전북(6), 전남(9)</td>
<td > </td>
<td >서울(8), 광주(1), 부산(11), 대전(2), 인천(1), 제주(4), 강원(6), 충북(2), 충남(3), 경북(3), 경남(1), 전북(6), 전남(5),경기(15)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >14</td>
<td >일반학교에서의 장애학생 지원</td>
<td >100</td>
<td >44</td>
<td >42</td>
<td >95.5%</td>
<td >42.0%</td>
<td > 1,200,000 </td>
<td > 2,280,000 </td>
<td >76.2</td>
<td >80.2 </td>
<td >5.26-6.6</td>
<td >6.16(<span>월</span><span>)~7.12(</span><span>금</span><span>)</span></td>
<td  >61(4주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(29)</td>
<td >18 </td>
<td >부산(26)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >15</td>
<td >장애인 등에 대한 특수교육법 및 관련법규의 이해</td>
<td >200</td>
<td >160</td>
<td >155</td>
<td >96.9%</td>
<td >77.5%</td>
<td > 2,520,000 </td>
<td > 7,155,000 </td>
<td >77</td>
<td >81.2 </td>
<td >6.2-6.13</td>
<td >6.16(<span>월</span><span>)~7.4(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(60)</td>
<td >103 </td>
<td >부산(57)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >16</td>
<td >특수교육관련 검사도구의 사용법</td>
<td >200</td>
<td >131</td>
<td >120</td>
<td >91.6%</td>
<td >60.0%</td>
<td > 2,115,000 </td>
<td > 5,760,000 </td>
<td >79.6</td>
<td >82.4 </td>
<td >6.2-6.13</td>
<td >6.16(<span>월</span><span>)~7.11(</span><span>금</span><span>)</span></td>
<td  >45(4주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(54)</td>
<td >81 </td>
<td >부산(50)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >17</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >600</td>
<td >556</td>
<td >533</td>
<td >95.9%</td>
<td >88.8%</td>
<td > 26,650,000 </td>
<td > 26,650,000 </td>
<td >82</td>
<td >84.8 </td>
<td >6.30-7.11</td>
<td >7.21(<span>월</span><span>)~8.29(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >해당 시<span>·</span><span>도 통합교사</span></td>
<td >1,100 </td>
<td >부산(300), 경남(800)</td>
<td >부산(211), 경남(376)</td>
<td > </td>
<td >부산(191), 경남(365)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >18</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >1,000</td>
<td >964</td>
<td >920</td>
<td >95.4%</td>
<td >92.0%</td>
<td > 46,000,000 </td>
<td > 46,000,000 </td>
<td >80.2</td>
<td >84.7 </td>
<td >7.14-7.25</td>
<td >8.4(<span>월</span><span>)~9.12(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >〃</td>
<td >1,000 </td>
<td >경기(1,000)</td>
<td >경기(1,000)</td>
<td > </td>
<td >경기(964)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >19</td>
<td >장애학생 이해와 통합학급 운영</td>
<td >100</td>
<td >59</td>
<td >49</td>
<td >83.1%</td>
<td >49.0%</td>
<td > 200,000 </td>
<td > 3,500,000 </td>
<td >77</td>
<td >84.7 </td>
<td >9.1-9.12</td>
<td >9.22(<span>월</span><span>)~10.18(</span><span>금</span><span>)</span></td>
<td  >61(4주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(4)</td>
<td >55 </td>
<td >부산(4)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >20</td>
<td >특수교육대상학생의 국어과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >379</td>
<td >339</td>
<td >89.4%</td>
<td >339.0%</td>
<td > 10,575,000 </td>
<td > 15,795,000 </td>
<td >77</td>
<td >81.4 </td>
<td >9.8-9.19</td>
<td >9.22(<span>월</span><span>)~10.10(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(18),경남(264)</td>
<td >116 </td>
<td >부산(16),경남(247)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >21</td>
<td >특수교육대상학생의 수학과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >273</td>
<td >244</td>
<td >89.4%</td>
<td >244.0%</td>
<td > 7,380,000 </td>
<td > 11,520,000 </td>
<td >78.2</td>
<td >83.2 </td>
<td >9.8-9.19</td>
<td >9.22(<span>월</span><span>)~10.10(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(18),경남(178)</td>
<td >92 </td>
<td >부산(17),경남(164)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >22</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >1,700</td>
<td >1,368</td>
<td >1,259</td>
<td >92.0%</td>
<td >74.1%</td>
<td > 62,950,000 </td>
<td > 62,950,000 </td>
<td >80.8</td>
<td >81.1 </td>
<td >9.15-9.26</td>
<td >10.6(<span>월</span><span>)~11.14(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >해당 시<span>·</span><span>도 통합교사</span></td>
<td  > </td>
<td > 서울(230),경기(1,206), 부산(300)</td>
<td > 서울(242),경기(1,198),부산(71)</td>
<td > </td>
<td > 서울(209),경기(1,102),부산(57)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >23</td>
<td >특수교육대상학생의 사회과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >180</td>
<td >172</td>
<td >95.6%</td>
<td >172.0%</td>
<td > 6,345,000 </td>
<td > 7,875,000 </td>
<td >77.6</td>
<td >80.8 </td>
<td >9.22-10.3</td>
<td >10.6(<span>월</span><span>)~10.24(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >경남(152),부산(12)</td>
<td >34 </td>
<td >경남(135),부산(11)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >24</td>
<td >특수교육대상학생의 과학과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >127</td>
<td >120</td>
<td >94.5%</td>
<td >120.0%</td>
<td > 4,635,000 </td>
<td > 5,445,000 </td>
<td >75.4</td>
<td >80.4 </td>
<td >9.22-10.3</td>
<td >10.6(<span>월</span><span>)~10.24(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >경남(116),부산(10)</td>
<td >18 </td>
<td >경남(99),부산(10)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >25</td>
<td >장애학생 진로<span>·</span><span>직업교육</span></td>
<td >100</td>
<td >213</td>
<td >199</td>
<td >93.4%</td>
<td >199.0%</td>
<td > 4,500,000 </td>
<td > 9,180,000 </td>
<td >79.8</td>
<td >83.8 </td>
<td >9.29-10.10</td>
<td >10.13(<span>월</span><span>)~10.31(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(21),경남(97)</td>
<td >104 </td>
<td >부산(19),경남(90)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >26</td>
<td >장애학생 이해와 통합학급 운영</td>
<td >100</td>
<td >33</td>
<td >30</td>
<td >90.9%</td>
<td >30.0%</td>
<td > 200,000 </td>
<td > 1,880,000 </td>
<td >86.2</td>
<td >90.3 </td>
<td >9.29-10.10</td>
<td >10.20(<span>월</span><span>)~11.15(</span><span>금</span><span>)</span></td>
<td  >61(4주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >부산(6명)</td>
<td >28 </td>
<td >부산(5명)</td>
</tr>

<tr>
<td>교원직무</td>
<td >정규과정</td>
<td >27</td>
<td >장애학생 이해와 통합학급 운영<span>1,2</span></td>
<td >200</td>
<td >189</td>
<td >173</td>
<td >91.5%</td>
<td >86.5%</td>
<td > 6,800,000 </td>
<td > 9,020,000 </td>
<td >81.6</td>
<td >84.8 </td>
<td >9.15-9.26</td>
<td >10.6(<span>월</span><span>)~11.15(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >해당 시<span>·</span><span>도 통합교사</span></td>
<td  > </td>
<td > </td>
<td >경남(115),세종(45)</td>
<td >37 </td>
<td >경남(110),세종(42)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >28</td>
<td >특수교육대상학생의 국어과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >72</td>
<td >61</td>
<td >84.7%</td>
<td >61.0%</td>
<td > 2,745,000 </td>
<td > 2,745,000 </td>
<td >82.6</td>
<td >84.0 </td>
<td >9.22-10.3</td>
<td >10.6(<span>월</span><span>)~10.24(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >경남(77명)</td>
<td > </td>
<td >경남(72명)</td>
</tr>

<tr>
<td>교원직무</td>
<td >특별과정</td>
<td >29</td>
<td >특수교육대상학생의 수학과 교수<span>·</span><span>학습 지도</span></td>
<td >100</td>
<td >60</td>
<td >51</td>
<td >85.0%</td>
<td >51.0%</td>
<td > 2,295,000 </td>
<td > 2,295,000 </td>
<td >76.6</td>
<td >82.4 </td>
<td >9.22-10.3</td>
<td >10.6(<span>월</span><span>)~10.24(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >교원 및 교육전문직</td>
<td  > </td>
<td > </td>
<td >경남(67명)</td>
<td > </td>
<td >경남(60명)</td>
</tr>

<tr>
<td> </td>
<td > </td>
<td colspan="2" >소계 <span>(29</span><span>과정</span><span>)</span></td>
<td >10,345</td>
<td >10,704</td>
<td >10,084</td>
<td >94.2%</td>
<td >97.5%</td>
<td > 439,290,000 </td>
<td > 501,060,000 </td>
<td >79.3</td>
<td >82.41</td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td >7,758 </td>
<td >9,499 </td>
<td >9,899 </td>
<td >1,282 </td>
<td >9,422</td>
</tr>

<tr>
<td>보조인력</td>
<td >특별과정</td>
<td >1</td>
<td >특수교육 보조인력 역량강화 심화과정<span>(</span><span>특별과정</span><span>)</span></td>
<td >400</td>
<td >373</td>
<td >347</td>
<td >93.0%</td>
<td >86.8%</td>
<td > 15,615,000 </td>
<td > 15,615,000 </td>
<td >77.4</td>
<td >81.4</td>
<td >3.24-4.4</td>
<td >4.7(<span>월</span><span>)~4.25(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >특수교육 보조인력</td>
<td >430 </td>
<td >서울(200), 제주(100), 경기(130)</td>
<td >서울(200), 제주(58), 경기(130)</td>
<td > </td>
<td >서울(194), 제주(54), 경기(125)</td>
</tr>

<tr>
<td>보조인력</td>
<td >정규과정</td>
<td >2</td>
<td >특수교육 보조인력 역량강화 기초과정</td>
<td >200</td>
<td >229</td>
<td >221</td>
<td >96.5%</td>
<td >110.5%</td>
<td > 3,015,000 </td>
<td > 10,215,000 </td>
<td >78.6</td>
<td >84.4</td>
<td >3.31-4.11</td>
<td >4.14(<span>월</span><span>)~5.2(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >특수교육 보조인력</td>
<td >200 </td>
<td >세종(100), 부산(100)</td>
<td >세종(6), 부산(66)</td>
<td >160 </td>
<td >세종(6), 부산(63)</td>
</tr>

<tr>
<td>보조인력</td>
<td >정규과정</td>
<td >3</td>
<td >특수교육 보조인력 역량강화 심화과정</td>
<td >200</td>
<td >612</td>
<td >580</td>
<td >94.8%</td>
<td >290.0%</td>
<td > 11,925,000 </td>
<td > 26,595,000 </td>
<td >79.4</td>
<td >84.8</td>
<td >3.31-4.11</td>
<td >4.14(<span>월</span><span>)~5.2(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >특수교육 보조인력</td>
<td >200 </td>
<td >전남(100), 부산(100)</td>
<td >전남(153), 경남(88), 부산(77)</td>
<td >326 </td>
<td >전남(124), 경남(87), 부산(75)</td>
</tr>

<tr>
<td>보조인력</td>
<td >정규과정</td>
<td >4</td>
<td >특수교육 보조인력 역량강화 기초과정</td>
<td >200</td>
<td >192</td>
<td >188</td>
<td >97.9%</td>
<td >94.0%</td>
<td > 4,590,000 </td>
<td > 8,460,000 </td>
<td >76</td>
<td >82.6</td>
<td >8.25-9.5</td>
<td >9.8(<span>월</span><span>)~9.26(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >특수교육 보조인력</td>
<td  > </td>
<td > </td>
<td >경남(43),경북(62),부산(11)</td>
<td >86 </td>
<td >경남(40),경북(58),부산(8)</td>
</tr>

<tr>
<td>보조인력</td>
<td >정규과정</td>
<td >5</td>
<td >특수교육 보조인력 역량강화 심화과정</td>
<td >300</td>
<td >465</td>
<td >443</td>
<td >95.3%</td>
<td >147.7%</td>
<td > 17,460,000 </td>
<td > 19,935,000 </td>
<td >74.8</td>
<td >82.1</td>
<td >8.25-9.5</td>
<td >9.8(<span>월</span><span>)~9.26(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >특수교육 보조인력</td>
<td >280 </td>
<td >부산(100), 경기(120), 제주(60)</td>
<td >제주(58),경기(287),부산(24),경남(18),경북(69)</td>
<td >55 </td>
<td >제주(45),경기(259),부산(23),경남(17),경북(66)</td>
</tr>

<tr>
<td>보조인력</td>
<td >정규과정</td>
<td >6</td>
<td >특수교육 보조인력 역량강화 기초과정</td>
<td >100</td>
<td >75</td>
<td >73</td>
<td >97.3%</td>
<td >73.0%</td>
<td > 2,070,000 </td>
<td > 3,285,000 </td>
<td >80.8</td>
<td >84.6</td>
<td >9.29-10.10</td>
<td >10.13(<span>월</span><span>)~10.31(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >특수교육 보조인력</td>
<td  > </td>
<td > </td>
<td >경남(10),경북(40)</td>
<td >27 </td>
<td >경남(9),경북(39)</td>
</tr>

<tr>
<td>보조인력</td>
<td >정규과정</td>
<td >7</td>
<td >특수교육 보조인력 역량강화 심화과정</td>
<td >100</td>
<td >92</td>
<td >92</td>
<td >100.0%</td>
<td >92.0%</td>
<td > 2,790,000 </td>
<td > 4,140,000 </td>
<td >76.8</td>
<td >82.2</td>
<td >10.13-10.24</td>
<td >10.27(<span>월</span><span>)~11.14(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >특수교육 보조인력</td>
<td  > </td>
<td > </td>
<td >경남(17),경북(51)</td>
<td >30 </td>
<td >경남(16),경북(46)</td>
</tr>

<tr>
<td> </td>
<td > </td>
<td colspan="2" >소계 <span>(7</span><span>과정</span><span>)</span></td>
<td >1,500</td>
<td >2,038</td>
<td >1,944</td>
<td >95.4%</td>
<td >129.6%</td>
<td > 57,465,000 </td>
<td > 88,245,000 </td>
<td >77.7</td>
<td >83.16</td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td >1,110 </td>
<td >1,270 </td>
<td>1,468 </td>
<td >684</td>
<td >1354</td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >학<span>1</span></td>
<td >학부모가 알아야할 특수교육개론</td>
<td >100</td>
<td >56</td>
<td >49</td>
<td >87.5%</td>
<td >49.0%</td>
<td > </td>
<td > </td>
<td >78.8</td>
<td >87.5</td>
<td >4.21-5.2</td>
<td >5.5(<span>월</span><span>)~5.23(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >56</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >학<span>2</span></td>
<td >가정에서의 장애학생 지원 방안</td>
<td >100</td>
<td >56</td>
<td >46</td>
<td >82.1%</td>
<td >46.0%</td>
<td > </td>
<td > </td>
<td >71.8</td>
<td >82.1</td>
<td >6.23-7.4</td>
<td >7.7(<span>월</span><span>)~7.25(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >56</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >학<span>3</span></td>
<td >학부모가 알아야할 치료지원 서비스</td>
<td >100</td>
<td >62</td>
<td >53</td>
<td >85.5%</td>
<td >53.0%</td>
<td > </td>
<td > </td>
<td >78</td>
<td >85.4</td>
<td >9.29-10.10</td>
<td >10.13(<span>월</span><span>)~10.31(</span><span>금</span><span>)</span></td>
<td  >30(3주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >62</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >학4</td>
<td >학부모가 알아야할 특수교육개론</td>
<td >100</td>
<td >99</td>
<td > </td>
<td >0.0%</td>
<td >0.0%</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td >11.19-12.7</td>
<td >12.9(월)~12.19(금)</td>
<td  >30(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >99</td>
<td > </td>
</tr>

<tr>
<td> </td>
<td > </td>
<td colspan="2" >소계<span>(4</span><span>개 과정</span><span>)</span></td>
<td >400</td>
<td >273</td>
<td >148</td>
<td >54.2%</td>
<td >37.0%</td>
<td > </td>
<td > </td>
<td >76.2</td>
<td >85.00 </td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td  > </td>
<td > </td>
<td > </td>
<td >273</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>1</span></td>
<td >수화기초과정</td>
<td >200</td>
<td >198</td>
<td >177</td>
<td >89.4%</td>
<td >88.5%</td>
<td > </td>
<td > </td>
<td >78.4</td>
<td >89.3</td>
<td >3.31-4.11</td>
<td >4.14(<span>월</span><span>)~4.25(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >198</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>2</span></td>
<td >시각장애학생 보행훈련</td>
<td >200</td>
<td >144</td>
<td >131</td>
<td >91.0%</td>
<td >65.5%</td>
<td > </td>
<td > </td>
<td >80.8</td>
<td >90.9</td>
<td >3.31-4.11</td>
<td >4.14(<span>월</span><span>)~4.25(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >144</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>3</span></td>
<td >장애학생의 인권과 성교육</td>
<td >200</td>
<td >201</td>
<td >191</td>
<td >95.0%</td>
<td >95.5%</td>
<td > </td>
<td > </td>
<td >83</td>
<td >95</td>
<td >4.14-4.25</td>
<td >4.28(<span>월</span><span>)~5.9(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >201</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>4</span></td>
<td >장애학생을 위한 특수교육지원능력 개발</td>
<td >200</td>
<td >200</td>
<td >181</td>
<td >90.5%</td>
<td >90.5%</td>
<td > </td>
<td > </td>
<td >83</td>
<td >90.5</td>
<td >4.14-4.25</td>
<td >4.28(<span>월</span><span>)~5.9(</span><span>금</span><span>)</span></td>
<td  >20(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >200</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>5</span></td>
<td >수화기초과정</td>
<td >200</td>
<td >300</td>
<td >270</td>
<td >90.0%</td>
<td >135.0%</td>
<td > </td>
<td > </td>
<td >78.6</td>
<td >90</td>
<td >6.23-7.4</td>
<td >7.7(<span>월</span><span>)~7.18(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >300</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>6</span></td>
<td >시각장애학생 보행훈련</td>
<td >200</td>
<td >160</td>
<td >145</td>
<td >90.6%</td>
<td >72.5%</td>
<td > </td>
<td > </td>
<td >80.4</td>
<td >90.6</td>
<td >6.23-7.4</td>
<td >7.7(<span>월</span><span>)~7.18(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >160</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>7</span></td>
<td >장애학생의 인권과 성교육</td>
<td >200</td>
<td >354</td>
<td >310</td>
<td >87.6%</td>
<td >155.0%</td>
<td > </td>
<td > </td>
<td >80.8</td>
<td >87.5</td>
<td >7.7-7.18</td>
<td >7.21(<span>월</span><span>)~8.1(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >354</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>8</span></td>
<td >장애학생을 위한 특수교육지원능력 개발</td>
<td >200</td>
<td >253</td>
<td >221</td>
<td >87.4%</td>
<td >110.5%</td>
<td > </td>
<td > </td>
<td >81.6</td>
<td >87.3</td>
<td >7.7-7.18</td>
<td >7.21(<span>월</span><span>)~8.1(</span><span>금</span><span>)</span></td>
<td  >20(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >253</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>9</span></td>
<td >수화기초과정</td>
<td >200</td>
<td >174</td>
<td >155</td>
<td >89.1%</td>
<td >77.5%</td>
<td > </td>
<td > </td>
<td >80.6</td>
<td >89</td>
<td >9.29-10.10</td>
<td >10.13(<span>월</span><span>)~10.24(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >174</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>10</span></td>
<td >시각장애학생 보행훈련</td>
<td >200</td>
<td >85</td>
<td >78</td>
<td >91.8%</td>
<td >39.0%</td>
<td > </td>
<td > </td>
<td >75.8</td>
<td >91.7</td>
<td >9.29-10.10</td>
<td >10.13(<span>월</span><span>)~10.24(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >85</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>11</span></td>
<td >장애학생의 인권과 성교육</td>
<td >200</td>
<td >200</td>
<td >169</td>
<td >84.5%</td>
<td >84.5%</td>
<td > </td>
<td > </td>
<td >81</td>
<td >84.5</td>
<td >10.13-10.24</td>
<td >10.27(<span>월</span><span>)~11.7(</span><span>금</span><span>)</span></td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >200</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기<span>12</span></td>
<td >장애학생을 위한 특수교육지원능력 개발</td>
<td >200</td>
<td >200</td>
<td >167</td>
<td >83.5%</td>
<td >83.5%</td>
<td > </td>
<td > </td>
<td >82.2</td>
<td >83.5</td>
<td >10.13-10.24</td>
<td >10.27(<span>월</span><span>)~11.7(</span><span>금</span><span>)</span></td>
<td  >20(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >200</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기13</td>
<td >수화기초과정</td>
<td >300</td>
<td >300</td>
<td > </td>
<td >0.0%</td>
<td >0.0%</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td >11.19-12.7</td>
<td >12.9(월)~12.19(금)</td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >300</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기14</td>
<td >시각장애학생 보행훈련</td>
<td >300</td>
<td >299</td>
<td > </td>
<td >0.0%</td>
<td >0.0%</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td >11.19-12.7</td>
<td >12.9(월)~12.19(금)</td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >299</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기15</td>
<td >장애학생의 인권과 성교육</td>
<td >500</td>
<td >500</td>
<td > </td>
<td >0.0%</td>
<td >0.0%</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td >11.19-12.7</td>
<td >12.9(월)~12.19(금)</td>
<td  >15(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >500</td>
<td > </td>
</tr>

<tr>
<td>일반연수</td>
<td >무료과정</td>
<td >기16</td>
<td >장애학생을 위한 특수교육지원능력 개발</td>
<td >300</td>
<td >300</td>
<td > </td>
<td >0.0%</td>
<td >0.0%</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td >11.19-12.7</td>
<td >12.9(월)~12.19(금)</td>
<td  >20(2주)</td>
<td >일반</td>
<td  > </td>
<td > </td>
<td > </td>
<td >300</td>
<td > </td>
</tr>



<tr>
<td colspan="4" >소계<span>(16</span><span>개 과정</span><span>)</span></td>
<td >3,800</td>
<td >3,868</td>
<td >2,195</td>
<td >56.7%</td>
<td >57.8%</td>
<td > </td>
<td > </td>
<td > 80.5 </td>
<td >89.15 </td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td  > </td>
<td > </td>
<td > </td>
<td >3,868</td>
<td > </td>
</tr>

<tr>
<td colspan="4">시.도 특별과정 계</td>
<td >8,345</td>
<td >8,351</td>
<td >7,895</td>
<td >94.5%</td>
<td >94.6%</td>
<td >393,225,000</td>
<td >393,225,000</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td  > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
</tr>

<tr>
<td  colspan="4">일반과정 계</td>
<td >7,700</td>
<td >8,532</td>
<td >6,476</td>
<td >75.9%</td>
<td >84.1%</td>
<td >103,530,000</td>
<td >196,080,000</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td  > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
</tr>

<tr>
<td  colspan="4">유료과정 계</td>
<td >11,845</td>
<td >12,742</td>
<td >12,028</td>
<td >94.4%</td>
<td >101.5%</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td  > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
</tr>

<tr>
<td  colspan="4" >무료과정 계</td>
<td >4,200</td>
<td >4,141</td>
<td >2,343</td>
<td >56.6%</td>
<td >55.8%</td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td  > </td>
<td > </td>
<td > </td>
<td > </td>
<td > </td>
</tr>

<tr>
<td  colspan="4">전체 소계(56)</td>
<td >16,045</td>
<td >16,883</td>
<td >14,371</td>
<td >85.1%</td>
<td >89.6%</td>
<td > 496,755,000 </td>
<td > 589,305,000 </td>
<td >79</td>
<td >82.79</td>
<td > </td>
<td > </td>
<td  > </td>
<td > </td>
<td >8,868 </td>
<td >10,769 </td>
<td>11,367 </td>
<td >6,107 </td>
<td >10,776 </td>
</tr>
</tbody>
</table>
<br><br><Br>
		</div>
		<!-- list table-->

</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/com/main/admYearEduStatusExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/com/main/admYearEduStatus.doo";
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

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
